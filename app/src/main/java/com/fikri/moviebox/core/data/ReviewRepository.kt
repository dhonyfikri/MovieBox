package com.fikri.moviebox.core.data

import com.fikri.moviebox.core.data.source.remote.RemoteDataSource
import com.fikri.moviebox.core.data.source.remote.response.ApiResultWrapper
import com.fikri.moviebox.core.domain.model.AuthorReviewDetails
import com.fikri.moviebox.core.domain.model.Review
import com.fikri.moviebox.core.domain.repository_interface.IReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReviewRepository(private val remoteDataSource: RemoteDataSource) : IReviewRepository {
    override fun getReview(apiKey: String, movieId: Int): Flow<Resource<Review>> {
        return flow {
            emit(Resource.Loading())
            when (val result = remoteDataSource.getReview(apiKey, movieId)) {
                is ApiResultWrapper.Success -> {
                    val listReview: ArrayList<Review> = arrayListOf()
                    result.response.results.map {
                        val authorDetails = AuthorReviewDetails(
                            it.authorDetails?.name,
                            it.authorDetails?.username,
                            it.authorDetails?.avatarPath,
                            it.authorDetails?.rating
                        )
                        listReview.add(
                            Review(
                                it.author,
                                authorDetails,
                                it.content,
                                it.createdAt,
                                it.id,
                                it.updatedAt,
                                it.url
                            )
                        )
                    }
                    emit(Resource.Success(listReview))
                }
                is ApiResultWrapper.Error -> {
                    val code: Int? = result.code
                    val failedType: String? = result.failedType
                    val message: String? = result.message
                    emit(Resource.Error(code, failedType, message))
                }
                is ApiResultWrapper.NetworkError -> {
                    val failedType: String? = result.failedType
                    val message: String? = result.message
                    emit(Resource.NetworkError(failedType, message))
                }
            }
        }
    }

    override fun getAllReviewList(apiKey: String, movieId: Int) =
        remoteDataSource.getAllReviewList(apiKey, movieId)
}