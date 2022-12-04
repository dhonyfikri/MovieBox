package com.fikri.moviebox.di

import com.fikri.moviebox.core.domain.use_case.*
import com.fikri.moviebox.view_model.GenreDiscoverViewModel
import com.fikri.moviebox.view_model.MainVewModel
import com.fikri.moviebox.view_model.MovieDetailViewModel
import com.fikri.moviebox.view_model.MovieReviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val useCaseModule = module {
        factory<GenreUseCase> { GenreInteractor(get()) }
        factory<MovieUseCse> { MovieInteractor(get()) }
        factory<ReviewUseCase> { ReviewInteractor(get()) }
        factory<VideoUseCase> { VideoInteractor(get()) }
    }

    val viewModelModule = module {
        viewModel { MainVewModel(get(), get()) }
        viewModel { MovieDetailViewModel(get(), get(), get()) }
        viewModel { GenreDiscoverViewModel(get()) }
        viewModel { MovieReviewViewModel(get()) }
    }
}