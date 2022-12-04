package com.fikri.moviebox

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fikri.moviebox.core.data.source.remote.network.ApiConfig
import com.fikri.moviebox.view_model.GenreDiscoverViewModel

class GenreDiscoverViewModelFactory() :
    ViewModelProvider.NewInstanceFactory() {

    private val apiService = ApiConfig.getApiService()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GenreDiscoverViewModel::class.java)) {
            return GenreDiscoverViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}