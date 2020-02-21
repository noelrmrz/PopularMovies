package com.noelrmrz.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.noelrmrz.popularmovies.utilities.GsonClient;
import com.noelrmrz.popularmovies.utilities.PicassoClient;

public class DetailActivity extends AppCompatActivity {

    private ImageView mThumbnail;
    private TextView mTitle;
    private TextView mRating;
    private TextView mDate;
    private TextView mSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mThumbnail = findViewById(R.id.iv_thumbnail);
        mTitle = findViewById(R.id.tv_title);
        mRating = findViewById(R.id.tv_rating);
        mDate = findViewById(R.id.tv_year);
        mSynopsis = findViewById(R.id.tv_synopsis);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                String movieJsonString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                Movie movie = GsonClient.getGsonClient().fromJson(movieJsonString, Movie.class);

                mTitle.setText(movie.getTitle());
                mRating.setText(movie.getmRating());
                mDate.setText(movie.getmDate());
                mSynopsis.setText(movie.getmSynopsis());
                PicassoClient.downloadImage(movie.getPosterPath(), mThumbnail);

                setTitle(movie.getTitle());
            }
        }

    }

}


