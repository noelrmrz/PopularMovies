package com.noelrmrz.popularmovies.utilities;

import com.noelrmrz.popularmovies.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TheMovieDbService {

    @GET("3/discover/movie")
    Call<MovieList> listMovies(@Query("api_key") String apiKey, @Query("language") String language,
                               @Query("sort_by") String sort, @Query("page") int page);

}
