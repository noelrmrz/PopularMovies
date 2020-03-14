package com.noelrmrz.popularmovies.utilities;

import com.noelrmrz.popularmovies.video.VideoList;
import com.noelrmrz.popularmovies.movie.MovieList;
import com.noelrmrz.popularmovies.review.ReviewList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbService {

    @GET("3/discover/movie")
    Call<MovieList> listMovies(@Query("api_key") String apiKey, @Query("language") String language,
                               @Query("sort_by") String sort, @Query("page") int page);

    @GET("3/movie/{movie_id}/reviews")
    Call<ReviewList> listReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey,
                                 @Query("page") int page);

    @GET("3/movie/{movie_id}/videos")
    Call<VideoList> listVideos(@Path("movie_id") int movieId, @Query("api_key") String apiKey,
                               @Query("type") String type, @Query("page") int page);
}
