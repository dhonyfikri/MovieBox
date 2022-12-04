package com.fikri.moviebox.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fikri.moviebox.core.domain.model.Genre
import com.fikri.moviebox.core.domain.model.Movie
import com.fikri.moviebox.core.domain.use_case.GenreUseCase
import com.fikri.moviebox.core.domain.use_case.MovieUseCse

class MainVewModel(private val genreUseCase: GenreUseCase, private val movieUseCse: MovieUseCse) :
    ViewModel() {

    private val _isInitialObserveFlow = MutableLiveData(false)
    val isInitialObserveFlow: LiveData<Boolean> = _isInitialObserveFlow
    private val _listMovie = MutableLiveData<ArrayList<Movie>>()
    val listMovie: LiveData<ArrayList<Movie>> = _listMovie
    private val _listGenre = MutableLiveData<ArrayList<Genre>>()
    val listGenre: LiveData<ArrayList<Genre>> = _listGenre
    var isGenreTabCollapsed = true

    fun getAllGenre(apiKey: String) = genreUseCase.getAllGenre(apiKey).asLiveData()
    fun getPopularMovie(apiKey: String) = movieUseCse.getPopularMovieList(apiKey).asLiveData()

    fun setFinishInitialObserveFlow() {
        _isInitialObserveFlow.value = true
    }

    fun setListMovie(list: ArrayList<Movie>){
        _listMovie.value = list
    }

    fun setListGenre(list: ArrayList<Genre>){
        _listGenre.value = list
    }
}