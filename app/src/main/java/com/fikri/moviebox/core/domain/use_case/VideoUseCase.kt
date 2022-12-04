package com.fikri.moviebox.core.domain.use_case

import com.fikri.moviebox.core.data.Resource
import com.fikri.moviebox.core.domain.model.MovieVideo
import kotlinx.coroutines.flow.Flow

interface VideoUseCase {
    fun getVideoTrailer(apiKey: String, movieId: Int): Flow<Resource<MovieVideo>>
}