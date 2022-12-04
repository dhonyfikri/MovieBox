package com.fikri.moviebox.core.di

import com.fikri.moviebox.core.data.GenreRepository
import com.fikri.moviebox.core.data.MovieRepository
import com.fikri.moviebox.core.data.ReviewRepository
import com.fikri.moviebox.core.data.VideoRepository
import com.fikri.moviebox.core.data.source.remote.RemoteDataSource
import com.fikri.moviebox.core.data.source.remote.network.ApiConfig
import com.fikri.moviebox.core.domain.repository_interface.IGenreRepository
import com.fikri.moviebox.core.domain.repository_interface.IMovieRepository
import com.fikri.moviebox.core.domain.repository_interface.IReviewRepository
import com.fikri.moviebox.core.domain.repository_interface.IVideoRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object CoreModule {
    val networkModule = module {
        single { ApiConfig.getApiService() }
    }
    val repositoryModule = module {
        single { RemoteDataSource(get()) }
        single<IGenreRepository> { GenreRepository(get()) }
        single<IMovieRepository> { MovieRepository(get()) }
        single<IReviewRepository> { ReviewRepository(get()) }
        single<IVideoRepository> { VideoRepository(get()) }
    }
}