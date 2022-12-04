package com.fikri.moviebox.core.domain.use_case

import com.fikri.moviebox.core.data.Resource
import com.fikri.moviebox.core.domain.model.Genre
import kotlinx.coroutines.flow.Flow

interface GenreUseCase {
    fun getAllGenre(apiKey: String): Flow<Resource<Genre>>
}