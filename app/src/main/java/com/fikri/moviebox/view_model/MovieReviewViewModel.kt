package com.fikri.moviebox.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fikri.moviebox.core.data.source.ReviewPagingSource
import com.fikri.moviebox.core.data.source.remote.network.ApiService
import com.fikri.moviebox.core.data.source.remote.network.Token
import com.fikri.moviebox.core.domain.model.Movie
import com.fikri.moviebox.core.domain.model.Review

class MovieReviewViewModel(private val apiService: ApiService) : ViewModel() {
    var selectedMovie: Movie? = null

    fun getAllReviewList(): LiveData<PagingData<Review>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = {
                ReviewPagingSource(apiService, Token.TMDB_TOKEN_V3, selectedMovie?.id ?: 0)
            }
        ).liveData.cachedIn(viewModelScope)
    }
}