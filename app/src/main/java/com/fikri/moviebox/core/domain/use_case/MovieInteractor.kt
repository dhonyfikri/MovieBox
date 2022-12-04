package com.fikri.moviebox.core.domain.use_case

import com.fikri.moviebox.core.domain.repository_interface.IMovieRepository

class MovieInteractor(private val movieRepository: IMovieRepository) : MovieUseCse {
    override fun getPopularMovieList(apiKey: String) = movieRepository.getPopularMovieList(apiKey)

    override fun getDetailMovie(apiKey: String, movieId: Int) =
        movieRepository.getDetailMovie(apiKey, movieId)

    override fun getMovieByGenre(apiKey: String, genreId: String) =
        movieRepository.getMovieByGenre(apiKey, genreId)
}