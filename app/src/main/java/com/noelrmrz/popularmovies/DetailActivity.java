package com.noelrmrz.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.popularmovies.database.MovieDatabase;
import com.noelrmrz.popularmovies.movie.Movie;
import com.noelrmrz.popularmovies.review.Review;
import com.noelrmrz.popularmovies.review.ReviewAdapter;
import com.noelrmrz.popularmovies.review.ReviewList;
import com.noelrmrz.popularmovies.utilities.GsonClient;
import com.noelrmrz.popularmovies.utilities.PicassoClient;
import com.noelrmrz.popularmovies.utilities.RetrofitClient;
import com.noelrmrz.popularmovies.video.VideoList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements ReviewAdapter.ReviewAdapterOnClickHandler {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private final String URL = "https://www.youtube.com/watch?v=";

    private ImageView mThumbnail;
    private TextView mTitle;
    private TextView mRating;
    private TextView mDate;
    private TextView mSynopsis;
    private Button mLikeButton;
    private Button mUnlikeButton;
    private Button mTrailerButton;

    private ReviewAdapter mReviewAdapter;
    private TextView mErrorMessageView;
    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private Movie mMovie;
    private MovieDatabase movieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTrailerButton = findViewById(R.id.button_trailer);
        mThumbnail = findViewById(R.id.iv_thumbnail);
        mTitle = findViewById(R.id.tv_title);
        mRating = findViewById(R.id.tv_rating);
        mDate = findViewById(R.id.tv_year);
        mSynopsis = findViewById(R.id.tv_synopsis);
        mLikeButton = findViewById(R.id.ib_like);
        mUnlikeButton = findViewById(R.id.ib_unlike);
        movieDatabase = MovieDatabase.getInstance(getApplicationContext());
        mErrorMessageView = findViewById(R.id.tv_error_message);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mRecyclerView = findViewById(R.id.rv_reviews);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                String movieJsonString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                mMovie = GsonClient.getGsonClient().fromJson(movieJsonString, Movie.class);

                mTitle.setText(mMovie.getMTitle());
                mRating.setText(mMovie.getMRating());
                mDate.setText(mMovie.getMDate());
                mSynopsis.setText(mMovie.getMSynopsis());
                PicassoClient.downloadImage(mMovie.getMPosterPath(), mThumbnail);

                setTitle(mMovie.getMTitle());
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);

        mReviewAdapter = new ReviewAdapter(this);

        mRecyclerView.setAdapter(mReviewAdapter);

        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mMovie.setMFavorite(true);
                        movieDatabase.movieDAO().insertFavoriteMovie(mMovie);
                    }
                });
            }
        });

        mUnlikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Only delete the movie from the DB if it is favorited
                if (mMovie.getMFavorite()) {
                    mMovie.setMFavorite(false);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            movieDatabase.movieDAO().deleteFavoriteMovie(mMovie);
                        }
                    });
                }
            }
        });

        mTrailerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                RetrofitClient.getMovieVideo(new Callback<VideoList>() {
                    @Override
                    public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                        if (response.isSuccessful()) {
                            VideoList videoList = response.body();

                            // Create the video intent
                            Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL +
                                    videoList.getMVideos().get(0).getmKey()));

                            // Verify there is an activity that can respond to the intent
                            PackageManager packageManager = getPackageManager();
                            List<ResolveInfo> activities = packageManager.queryIntentActivities(videoIntent,
                                    PackageManager.MATCH_DEFAULT_ONLY);
                            boolean isIntentSafe = activities.size() > 0;

                            // If true start the intent, else show an error message
                            if (isIntentSafe) {
                                startActivity(Intent.createChooser(videoIntent, "View with..."));
                            } else {
                            }

                        } else {
                            Log.v(TAG , response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoList> call, Throwable t) {

                    }
                }, mMovie.getMId());
            }
        });

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Check for network connectivity before fetching the data
        if (isConnected) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            loadMovieReviews(mMovie.getMId());
        } else {
            mErrorMessageView.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    private void loadMovieReviews(int movieId) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        RetrofitClient.getMovieReview(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {

                if (response.isSuccessful()) {
                    ReviewList reviewList = response.body();
                    mReviewAdapter.setmReviewList(reviewList);
                } else {
                    Log.v(TAG , response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
                mErrorMessageView.setVisibility(View.VISIBLE);
                Log.v(TAG, t.toString());
            }
        }, movieId);
    }

    @Override
    public void onClick(Review review) {

    }
}


