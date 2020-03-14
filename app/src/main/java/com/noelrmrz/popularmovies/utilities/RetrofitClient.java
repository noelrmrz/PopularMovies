package com.noelrmrz.popularmovies.utilities;

import com.noelrmrz.popularmovies.video.VideoList;
import com.noelrmrz.popularmovies.movie.MovieList;
import com.noelrmrz.popularmovies.review.ReviewList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.themoviedb.org/";
    // Request your own API_KEY form themoviedb.org and place the key into the variable below
    private static final String API_KEY = "";

    public static void getMovieObject(Callback<MovieList> callback, String preference) {
        Retrofit retrofit = retrofitBuilder();

        TheMovieDbService service = retrofit.create(TheMovieDbService.class);
        Call<MovieList> movies = service.listMovies(API_KEY, "en", preference, 1);

        movies.enqueue(callback);
    }

    public static void getMovieReview(Callback<ReviewList> callback, int movieId) {
        Retrofit retrofit = retrofitBuilder();

        TheMovieDbService service = retrofit.create(TheMovieDbService.class);

        Call<ReviewList> reviews = service.listReviews(movieId, API_KEY, 1);

        reviews.enqueue(callback);
    }

    public static void getMovieVideo(Callback<VideoList> callback, int movieId) {
        Retrofit retrofit = retrofitBuilder();

        TheMovieDbService service = retrofit.create(TheMovieDbService.class);

        Call<VideoList> videos = service.listVideos(movieId, API_KEY, "Trailer", 1);

        videos.enqueue(callback);
    }

    private static Retrofit retrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
