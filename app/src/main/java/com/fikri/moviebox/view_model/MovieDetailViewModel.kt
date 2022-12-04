package com.fikri.moviebox.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fikri.moviebox.core.domain.model.Movie
import com.fikri.moviebox.core.domain.model.MovieDetail
import com.fikri.moviebox.core.domain.model.MovieVideo
import com.fikri.moviebox.core.domain.model.Review
import com.fikri.moviebox.core.domain.use_case.MovieUseCse
import com.fikri.moviebox.core.domain.use_case.ReviewUseCase
import com.fikri.moviebox.core.domain.use_case.VideoUseCase

class MovieDetailViewModel(
    private val movieUseCse: MovieUseCse,
    private val reviewUseCase: ReviewUseCase,
    private val videoUseCase: VideoUseCase
) :
    ViewModel() {

    private val _isInitialObserveFlow = MutableLiveData(false)
    val isInitialObserveFlow: LiveData<Boolean> = _isInitialObserveFlow
    private val _movieDetail = MutableLiveData<MovieDetail>()
    val movieDetail: LiveData<MovieDetail> = _movieDetail
    private val _listReview = MutableLiveData<ArrayList<Review>>()
    val listReview: LiveData<ArrayList<Review>> = _listReview
    private val _listMovieVideo = MutableLiveData<ArrayList<MovieVideo>>()
    val listMovieVideo: LiveData<ArrayList<MovieVideo>> = _listMovieVideo
    lateinit var selectedMovie: Movie

    fun getDetailMovie(apiKey: String, movieId: Int) =
        movieUseCse.getDetailMovie(apiKey, movieId).asLiveData()

    fun getReview(apiKey: String, movieId: Int) =
        reviewUseCase.getReview(apiKey, movieId).asLiveData()

    fun getVideoTrailer(apiKey: String, movieId: Int) =
        videoUseCase.getVideoTrailer(apiKey, movieId).asLiveData()

    fun setFinishInitialObserveFlow() {
        _isInitialObserveFlow.value = true
    }

    fun setMovieDetailValue(value: MovieDetail) {
        _movieDetail.value = value
    }

    fun setListReviewValue(value: ArrayList<Review>) {
        _listReview.value = value
    }

    fun setListMovieVideoValue(value: ArrayList<MovieVideo>) {
        _listMovieVideo.value = value
    }
}