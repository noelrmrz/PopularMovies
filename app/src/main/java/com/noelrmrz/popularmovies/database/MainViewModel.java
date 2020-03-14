package com.noelrmrz.popularmovies.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.noelrmrz.popularmovies.movie.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> favoriteMovies;

    public MainViewModel (Application application) {
        super(application);

        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        favoriteMovies = movieDatabase.movieDAO().loadFavoriteMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }
}
