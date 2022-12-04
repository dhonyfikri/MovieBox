package com.fikri.moviebox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fikri.moviebox.core.data.source.remote.network.ApiConfig
import com.fikri.moviebox.view_model.MovieReviewViewModel

class MovieReviewViewModelFactory() :
    ViewModelProvider.NewInstanceFactory() {

    private val apiService = ApiConfig.getApiService()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieReviewViewModel::class.java)) {
            return MovieReviewViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}