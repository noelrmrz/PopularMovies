package com.noelrmrz.popularmovies.movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieList {

    @SerializedName("results")
    private List<Movie> mMovieList;

    public MovieList(List<Movie> movieList) {
        mMovieList = movieList;
    }

    public List<Movie> getMovieList() {
        return mMovieList;
    }

    public void setMovieList(List<Movie> movieList) {
        mMovieList = movieList;
    }
}
