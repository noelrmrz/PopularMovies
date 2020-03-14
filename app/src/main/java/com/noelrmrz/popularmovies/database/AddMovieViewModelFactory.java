package com.noelrmrz.popularmovies.database;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AddMovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieDatabase mDb;
    private final int mMovieId;

    public AddMovieViewModelFactory(MovieDatabase database, int movieId) {
        mDb = database;
        mMovieId =  movieId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AddMovieViewModel(mDb, mMovieId);
    }
}
