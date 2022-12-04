package com.fikri.moviebox.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fikri.moviebox.core.data.source.MoviePagingSource
import com.fikri.moviebox.core.data.source.remote.network.ApiService
import com.fikri.moviebox.core.data.source.remote.network.Server
import com.fikri.moviebox.core.data.source.remote.network.Token
import com.fikri.moviebox.core.domain.model.Genre
import com.fikri.moviebox.core.domain.model.Movie

class GenreDiscoverViewModel(private val apiService: ApiService) : ViewModel() {
    var selectedGenre: Genre? = null
    var listOfGenre: ArrayList<Genre> = arrayListOf()

    fun getMovieByGenre(): LiveData<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = {
                MoviePagingSource(apiService, Token.TMDB_TOKEN_V3, selectedGenre?.id.toString())
            }
        ).liveData.cachedIn(viewModelScope)
    }
}