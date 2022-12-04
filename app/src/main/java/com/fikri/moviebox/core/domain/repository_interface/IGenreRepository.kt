package com.fikri.moviebox.core.domain.repository_interface

import com.fikri.moviebox.core.data.Resource
import com.fikri.moviebox.core.domain.model.Genre
import kotlinx.coroutines.flow.Flow

interface IGenreRepository {
    fun getAllGenre(apiKey: String): Flow<Resource<Genre>>
}