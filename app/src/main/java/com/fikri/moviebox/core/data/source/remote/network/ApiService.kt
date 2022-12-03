package com.fikri.moviebox.core.data.source.remote.network

import com.fikri.moviebox.core.data.source.remote.response.GenreListResponse
import com.fikri.moviebox.core.data.source.remote.response.MovieListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

//    @FormUrlEncoded
//    @POST("register")
//    fun register(
//        @Field("name") name: String,
//        @Field("email") email: String,
//        @Field("password") password: String,
//    ): Call<RegisterResponseModel>
//
//    @FormUrlEncoded
//    @POST("login")
//    fun login(
//        @Field("email") email: String,
//        @Field("password") password: String,
//    ): Call<LoginResponseModel>
//
//    @GET("stories")
//    suspend fun getAllBasicStories(
//        @Header("Authorization") authorization: String,
//        @Query("page") page: Int,
//        @Query("size") size: Int,
//        @Query("location") locationEnable: Int
//    ): AllStoryResponseModel
//
//    @Multipart
//    @POST("stories")
//    fun addStory(
//        @Header("Authorization") authorization: String,
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody
//    ): Call<AddStoryResponseModel>
//
//    @GET("stories")
//    fun getAllMapStories(
//        @Header("Authorization") authorization: String,
//        @Query("size") size: Int,
//        @Query("location") locationEnable: Int
//    ): Call<AllStoryResponseModel>
//
//    @Multipart
//    @POST("stories")
//    fun addGeolocationStory(
//        @Header("Authorization") authorization: String,
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody,
//        @Part("lat") latitude: RequestBody,
//        @Part("lon") longitude: RequestBody
//    ): Call<AddStoryResponseModel>


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
}