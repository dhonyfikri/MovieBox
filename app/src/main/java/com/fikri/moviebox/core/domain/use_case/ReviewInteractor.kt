package com.fikri.moviebox.core.domain.use_case

import com.fikri.moviebox.core.domain.repository_interface.IReviewRepository

class ReviewInteractor(private val reviewRepository: IReviewRepository) : ReviewUseCase {
    override fun getReview(apiKey: String, movieId: Int) =
        reviewRepository.getReview(apiKey, movieId)

    override fun getAllReviewList(apiKey: String, movieId: Int) =
        reviewRepository.getAllReviewList(apiKey, movieId)
}