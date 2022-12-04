package com.fikri.moviebox.core.data.source.remote

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.fikri.moviebox.core.data.source.MoviePagingSource
import com.fikri.moviebox.core.data.source.ReviewPagingSource
import com.fikri.moviebox.core.data.source.remote.network.ApiService
import com.fikri.moviebox.core.data.source.remote.response.*
import com.fikri.moviebox.core.domain.model.Movie
import com.fikri.moviebox.core.domain.model.Review
import com.fikri.moviebox.core.ui.modal.ResponseModal
import org.json.JSONObject
import retrofit2.Response
import retrofit2.awaitResponse
import java.io.IOException

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getAllGenre(
        apiKey: String
    ): ApiResultWrapper<GenreListResponse> {
        val apiRequest = apiService.getAllGenre(apiKey = apiKey)

        try {
            val response: Response<GenreListResponse> = apiRequest.awaitResponse()
            if (response.isSuccessful) {
                val responseBody = response.body()
                return if (responseBody != null) {
                    ApiResultWrapper.Success(responseBody, "Success get data")
                } else {
                    ApiResultWrapper.Error(
                        response.code(),
                        ResponseModal.TYPE_FAILED,
                        "Broken Data"
                    )
                }
            } else {
                var errorMessage: String? = null
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage = jObjError.getString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ApiResultWrapper.Error(
                    response.code(),
                    ResponseModal.TYPE_MISTAKE,
                    "${response.message()} | $errorMessage"
                )
            }
        } catch (e: IOException) {
            return ApiResultWrapper.NetworkError(
                ResponseModal.TYPE_ERROR,
                "Connection Failed"
            )
        }
    }

    suspend fun getPopularMovieList(apiKey: String): ApiResultWrapper<MovieListResponse> {
        val apiRequest = apiService.getListMovie(apiKey = apiKey)

        try {
            val response: Response<MovieListResponse> = apiRequest.awaitResponse()
            if (response.isSuccessful) {
                val responseBody = response.body()
                return if (responseBody != null) {
                    ApiResultWrapper.Success(responseBody, "Success get data")
                } else {
                    ApiResultWrapper.Error(
                        response.code(),
                        ResponseModal.TYPE_FAILED,
                        "Broken Data"
                    )
                }
            } else {
                var errorMessage: String? = null
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage = jObjError.getString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ApiResultWrapper.Error(
                    response.code(),
                    ResponseModal.TYPE_MISTAKE,
                    "${response.message()} | $errorMessage"
                )
            }
        } catch (e: IOException) {
            return ApiResultWrapper.NetworkError(
                ResponseModal.TYPE_ERROR,
                "Connection Failed"
            )
        }
    }

    fun getMovieByGenre(apiKey: String, genreId: String): LiveData<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = {
                MoviePagingSource(apiService, apiKey, genreId)
            }
        ).liveData
    }

    suspend fun getDetailMovie(
        apiKey: String,
        movieId: Int
    ): ApiResultWrapper<MovieDetailResponse> {
        val apiRequest = apiService.getDetailMovie(apiKey = apiKey, movieId = movieId)

        try {
            val response: Response<MovieDetailResponse> = apiRequest.awaitResponse()
            if (response.isSuccessful) {
                val responseBody = response.body()
                return if (responseBody != null) {
                    ApiResultWrapper.Success(responseBody, "Success get data")
                } else {
                    ApiResultWrapper.Error(
                        response.code(),
                        ResponseModal.TYPE_FAILED,
                        "Broken Data"
                    )
                }
            } else {
                var errorMessage: String? = null
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage = jObjError.getString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ApiResultWrapper.Error(
                    response.code(),
                    ResponseModal.TYPE_MISTAKE,
                    "${response.message()} | $errorMessage"
                )
            }
        } catch (e: IOException) {
            return ApiResultWrapper.NetworkError(
                ResponseModal.TYPE_ERROR,
                "Connection Failed"
            )
        }
    }

    suspend fun getVideoTrailer(
        apiKey: String,
        movieId: Int
    ): ApiResultWrapper<MovieVideoListResponse> {
        val apiRequest = apiService.getMovieVideo(apiKey = apiKey, movieId = movieId)

        try {
            val response: Response<MovieVideoListResponse> = apiRequest.awaitResponse()
            if (response.isSuccessful) {
                val responseBody = response.body()
                return if (responseBody != null) {
                    ApiResultWrapper.Success(responseBody, "Success get data")
                } else {
                    ApiResultWrapper.Error(
                        response.code(),
                        ResponseModal.TYPE_FAILED,
                        "Broken Data"
                    )
                }
            } else {
                var errorMessage: String? = null
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage = jObjError.getString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ApiResultWrapper.Error(
                    response.code(),
                    ResponseModal.TYPE_MISTAKE,
                    "${response.message()} | $errorMessage"
                )
            }
        } catch (e: IOException) {
            return ApiResultWrapper.NetworkError(
                ResponseModal.TYPE_ERROR,
                "Connection Failed"
            )
        }
    }

    suspend fun getReview(apiKey: String, movieId: Int): ApiResultWrapper<ReviewListResponse> {
        val apiRequest = apiService.getListReview(apiKey = apiKey, movieId = movieId)

        try {
            val response: Response<ReviewListResponse> = apiRequest.awaitResponse()
            if (response.isSuccessful) {
                val responseBody = response.body()
                return if (responseBody != null) {
                    ApiResultWrapper.Success(responseBody, "Success get data")
                } else {
                    ApiResultWrapper.Error(
                        response.code(),
                        ResponseModal.TYPE_FAILED,
                        "Broken Data"
                    )
                }
            } else {
                var errorMessage: String? = null
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage = jObjError.getString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ApiResultWrapper.Error(
                    response.code(),
                    ResponseModal.TYPE_MISTAKE,
                    "${response.message()} | $errorMessage"
                )
            }
        } catch (e: IOException) {
            return ApiResultWrapper.NetworkError(
                ResponseModal.TYPE_ERROR,
                "Connection Failed"
            )
        }
    }

    fun getAllReviewList(apiKey: String, movieId: Int): LiveData<PagingData<Review>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = {
                ReviewPagingSource(apiService, apiKey, movieId)
            }
        ).liveData
    }
}