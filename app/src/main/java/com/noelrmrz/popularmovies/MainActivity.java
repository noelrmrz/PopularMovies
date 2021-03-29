package com.noelrmrz.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.popularmovies.database.MainViewModel;
import com.noelrmrz.popularmovies.database.MainViewModelFactory;
import com.noelrmrz.popularmovies.movie.Movie;
import com.noelrmrz.popularmovies.movie.MovieAdapter;
import com.noelrmrz.popularmovies.movie.MovieList;
import com.noelrmrz.popularmovies.settings.SettingsActivity;
import com.noelrmrz.popularmovies.utilities.GsonClient;
import com.noelrmrz.popularmovies.utilities.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {

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
                calculateBestSpanCount(500));

        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        setupSharedPreferences();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Check for network connectivity before fetching the data
        if (isConnected) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            loadMovieData(PreferenceManager.getDefaultSharedPreferences(this).
                    getString(getString(R.string.pref_sort_key),
                            getString(R.string.sort_by_popularity)));
        } else {
            mErrorMessageView.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    private void loadMovieData(String preference) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (preference.equals(getString(R.string.favorites_value))) {
            setUpViewModel();
        }
        else {
            RetrofitClient.getMovieObject(new Callback<MovieList>() {
                @Override
                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                    if (response.isSuccessful()) {
                        MovieList movieList = response.body();
                        mMovieAdapter.setMovieList(movieList);
                    } else {
                        Log.v(TAG, response.errorBody().toString());
                        Log.v(TAG, "error");
                    }
                }

                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {
                    mErrorMessageView.setVisibility(View.VISIBLE);
                    Log.v(TAG, t.toString());
                    Log.v(TAG, "failure");
                }
            }, preference);
        }
    }

    /**
     *
     * @param movie object whose details will be displayed in Detail Activity
     */
    @Override
    public void onClick(Movie movie) {
        Context context = this;

        // Convert the Movie POJO to a JSON string using GSON
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
        // Start Settings Activity if the id matches
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent
                        (this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setUpViewModel() {
        MainViewModel viewModel = new ViewModelProvider(this,
                new MainViewModelFactory(this.getApplication())).get(MainViewModel.class);
        viewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies == null) {
                    Toast.makeText(getBaseContext(), "no favorites", Toast.LENGTH_SHORT);
                }
                else {
                    mMovieAdapter.setMovieList(new MovieList(movies));
                }
            }
        });
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_key))) {
            loadMovieData(sharedPreferences.getString(key, getString(R.string.sort_by_popularity)));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);
    }
}
