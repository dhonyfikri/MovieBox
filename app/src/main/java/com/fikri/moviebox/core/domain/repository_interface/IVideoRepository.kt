package com.fikri.moviebox.core.domain.repository_interface

import com.fikri.moviebox.core.data.Resource
import com.fikri.moviebox.core.domain.model.MovieVideo
import kotlinx.coroutines.flow.Flow

interface IVideoRepository {
    fun getVideoTrailer(apiKey: String, movieId: Int): Flow<Resource<MovieVideo>>
}