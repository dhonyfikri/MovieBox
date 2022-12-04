package com.fikri.moviebox.core.domain.use_case

import com.fikri.moviebox.core.domain.repository_interface.IVideoRepository

class VideoInteractor(private val videoRepository: IVideoRepository) : VideoUseCase {
    override fun getVideoTrailer(apiKey: String, movieId: Int) =
        videoRepository.getVideoTrailer(apiKey, movieId)
}