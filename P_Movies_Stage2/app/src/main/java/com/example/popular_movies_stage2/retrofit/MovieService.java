package com.example.popular_movies_stage2.retrofit;

import com.example.popular_movies_stage2.model.Movie;
import com.example.popular_movies_stage2.model.MovieResult;
import com.example.popular_movies_stage2.model.MovieReview;
import com.example.popular_movies_stage2.model.MovieVideo;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("{filter}")
    Call<MovieResult> getMovies(@Path("filter") String filter, @Query("api_key") String apiKey);

    @GET("{movieId}")
    Call<Movie> getMovie(@Path("movieId") String movieId, @Query("api_key") String apiKey);

    @GET("{movieId}/videos")
    Call<MovieVideo> getTrailer(@Path("movieId") String movieId, @Query("api_key") String apiKey);

    @GET("{movieId}/reviews")
    Call<MovieReview> getReviews(@Path("movieId") String movieId, @Query("api_key") String apiKey);

}
