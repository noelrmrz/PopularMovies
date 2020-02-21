package com.noelrmrz.popularmovies.utilities;

import com.noelrmrz.popularmovies.MovieList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.themoviedb.org/";
    // Request your own API_KEY form themoviedb.org and place the key into the variable below
    private static final String API_KEY = "";
    private static String sort = "popularity.desc";

    public static void getMovieObject(Callback<MovieList> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDbService service = retrofit.create(TheMovieDbService.class);
        Call<MovieList> movies = service.listMovies(API_KEY, "en", sort, 1);

        movies.enqueue(callback);
    }

    public static void changeSortOrder(String sortOrder) {
        sort = sortOrder;
    }
}
