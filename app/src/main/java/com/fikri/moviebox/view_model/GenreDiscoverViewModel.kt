package com.fikri.moviebox.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fikri.moviebox.core.domain.model.Genre
import com.fikri.moviebox.core.domain.use_case.MovieUseCse

class GenreDiscoverViewModel(private val movieUseCse: MovieUseCse) : ViewModel() {
    var selectedGenre: Genre? = null
    var listOfGenre: ArrayList<Genre> = arrayListOf()

    fun getMovieByGenre(apiKey: String) =
        movieUseCse.getMovieByGenre(apiKey, selectedGenre?.id.toString()).cachedIn(viewModelScope)
}