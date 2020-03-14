package com.noelrmrz.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.noelrmrz.popularmovies.movie.Movie;

import java.util.List;

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM favorite_movies")
    LiveData<List<Movie>> loadFavoriteMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavoriteMovie(Movie movie);

    @Delete
    void deleteFavoriteMovie(Movie movie);

    @Query("SELECT * FROM favorite_movies WHERE id = :id")
    LiveData<Movie> loadMovieById(int id);
}
