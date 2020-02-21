package com.noelrmrz.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.popularmovies.utilities.GsonClient;
import com.noelrmrz.popularmovies.utilities.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageView;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movie_posters);
        mErrorMessageView = findViewById(R.id.tv_error_message);
        GridLayoutManager layoutManager = new GridLayoutManager(this,
                calculateBestSpanCount(R.dimen.poster_width));

        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Check for network connectivity before fetching the data
        if (isConnected) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            loadMovieData();
        } else {
            mErrorMessageView.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    private void loadMovieData() {
        RetrofitClient.getMovieObject(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                if (response.isSuccessful()) {
                    MovieList movieList = response.body();
                    mMovieAdapter.setMovieList(movieList);
                } else {
                    Log.v(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                mErrorMessageView.setVisibility(View.VISIBLE);
                Log.v(TAG, t.toString());
            }
        });
    }

    /**
     *
     * @param movie object whose details will be displayed in a new activity
     */
    @Override
    public void onClick(Movie movie) {
        Context context = this;
        String movieJsonString = GsonClient.getGsonClient().toJson(movie);
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movieJsonString);
        startActivity(intentToStartDetailActivity);
    }

    /**
     *
     * @param posterWidth the width of the image in px
     * @return the appropriate number of columns to span based image width
     */
    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mLoadingIndicator.setVisibility(View.VISIBLE);

        // Fetch new data based on the selected sorting order
        switch (item.getItemId()) {
            case R.id.sort_popularity:
                RetrofitClient.changeSortOrder(getString(R.string.sort_by_popularity));
                loadMovieData();
                return true;
            case R.id.sort_rating:
                RetrofitClient.changeSortOrder(getString(R.string.sort_by_rating));
                loadMovieData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
