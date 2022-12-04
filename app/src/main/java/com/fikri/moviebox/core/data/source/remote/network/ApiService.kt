package com.fikri.moviebox.core.data.source.remote.network

import com.fikri.moviebox.core.data.source.remote.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("genre/movie/list")
    fun getAllGenre(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): Call<GenreListResponse>

    @GET("discover/movie")
    fun getListMovie(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("page") page: Int = 1,
        @Query("with_watch_monetization_types") withWatchMonetizationTypes: String = "flatrate",
    ): Call<MovieListResponse>

    @GET("movie/{movie_id}")
    fun getDetailMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
    ): Call<MovieDetailResponse>

    @GET("movie/{movie_id}/reviews")
    fun getListReview(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
    ): Call<ReviewListResponse>

    @GET("movie/{movie_id}/videos")
    fun getMovieVideo(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
    ): Call<MovieVideoListResponse>
}