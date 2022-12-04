package com.fikri.moviebox.core.domain.repository_interface

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.fikri.moviebox.core.data.Resource
import com.fikri.moviebox.core.domain.model.Review
import kotlinx.coroutines.flow.Flow

interface IReviewRepository {
    fun getReview(apiKey: String, movieId: Int): Flow<Resource<Review>>
    fun getAllReviewList(apiKey: String, movieId: Int): LiveData<PagingData<Review>>
}