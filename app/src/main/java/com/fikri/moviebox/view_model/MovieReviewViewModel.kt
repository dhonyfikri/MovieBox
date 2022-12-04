package com.fikri.moviebox.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fikri.moviebox.core.domain.model.Movie
import com.fikri.moviebox.core.domain.use_case.ReviewUseCase

class MovieReviewViewModel(private val reviewUseCase: ReviewUseCase) : ViewModel() {
    var selectedMovie: Movie? = null

    fun getAllReviewList(apiKey: String) =
        reviewUseCase.getAllReviewList(apiKey, selectedMovie?.id ?: 0).cachedIn(viewModelScope)
}