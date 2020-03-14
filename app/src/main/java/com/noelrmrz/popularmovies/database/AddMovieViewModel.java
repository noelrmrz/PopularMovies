package com.noelrmrz.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.noelrmrz.popularmovies.movie.Movie;

public class AddMovieViewModel extends ViewModel {

    private LiveData<Movie> movie;

    public AddMovieViewModel(MovieDatabase database, int movieId) {
        movie = database.movieDAO().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }
}
