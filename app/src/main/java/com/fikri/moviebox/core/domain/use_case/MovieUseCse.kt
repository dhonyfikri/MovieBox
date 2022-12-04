package com.fikri.moviebox.core.domain.use_case

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.fikri.moviebox.core.data.Resource
import com.fikri.moviebox.core.domain.model.Movie
import com.fikri.moviebox.core.domain.model.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MovieUseCse {
    fun getPopularMovieList(apiKey: String): Flow<Resource<Movie>>
    fun getDetailMovie(apiKey: String, movieId: Int): Flow<Resource<MovieDetail>>
    fun getMovieByGenre(apiKey: String, genreId: String): LiveData<PagingData<Movie>>
}