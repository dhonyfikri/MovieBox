package com.fikri.moviebox.core.data.source.remote.network

sealed class ApiResultWrapper<out T> {
    data class Success<out T>(val response: T, val message: String? = null) : ApiResultWrapper<T>()
    data class Error(
        val code: Int? = null,
        val failedType: String? = null,
        val message: String? = null
    ) : ApiResultWrapper<Nothing>()

    data class NetworkError(val failedType: String? = null, val message: String? = null) :
        ApiResultWrapper<Nothing>()
}