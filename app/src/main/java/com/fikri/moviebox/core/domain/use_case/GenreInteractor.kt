package com.fikri.moviebox.core.domain.use_case

import com.fikri.moviebox.core.data.Resource
import com.fikri.moviebox.core.domain.model.Genre
import com.fikri.moviebox.core.domain.repository_interface.IGenreRepository
import kotlinx.coroutines.flow.Flow

class GenreInteractor(private val genreRepository: IGenreRepository): GenreUseCase {
    override fun getAllGenre(apiKey: String) = genreRepository.getAllGenre(apiKey)
}