package com.fikri.moviebox.core.data

import com.fikri.moviebox.core.data.source.remote.RemoteDataSource
import com.fikri.moviebox.core.data.source.remote.response.ApiResultWrapper
import com.fikri.moviebox.core.domain.model.Genre
import com.fikri.moviebox.core.domain.model.Movie
import com.fikri.moviebox.core.domain.model.MovieDetail
import com.fikri.moviebox.core.domain.model.ProductionCompanies
import com.fikri.moviebox.core.domain.repository_interface.IMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepository(private val remoteDataSource: RemoteDataSource) : IMovieRepository {
    override fun getPopularMovieList(apiKey: String): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading())
            when (val result = remoteDataSource.getPopularMovieList(apiKey)) {
                is ApiResultWrapper.Success -> {
                    val listMovie: ArrayList<Movie> = arrayListOf()
                    result.response.results.forEach {
                        val movie = Movie(
                            it.adult,
                            it.backdropPath,
                            it.genreIds,
                            it.id,
                            it.originalLanguage,
                            it.originalTitle,
                            it.overview,
                            it.popularity,
                            it.posterPath,
                            it.releaseDate,
                            it.title,
                            it.video,
                            it.voteAverage,
                            it.voteCount
                        )
                        listMovie.add(movie)
                    }
                    emit(Resource.Success(listMovie))
                }
                is ApiResultWrapper.Error -> {
                    val code: Int? = result.code
                    val failedType: String? = result.failedType
                    val message: String? = result.message
                    emit(Resource.Error(code, failedType, message))
                }
                is ApiResultWrapper.NetworkError -> {
                    val failedType: String? = result.failedType
                    val message: String? = result.message
                    emit(Resource.NetworkError(failedType, message))
                }
            }
        }
    }

    override fun getDetailMovie(apiKey: String, movieId: Int): Flow<Resource<MovieDetail>> {
        return flow {
            emit(Resource.Loading())
            when (val result = remoteDataSource.getDetailMovie(apiKey, movieId)) {
                is ApiResultWrapper.Success -> {
                    val genres =
                        result.response.genres.map { Genre(it.id, it.name) } as ArrayList<Genre>
                    val productionCompanies = result.response.productionCompanies.map {
                        ProductionCompanies(
                            it.id,
                            it.logoPath,
                            it.name,
                            it.originCountry
                        )
                    } as ArrayList<ProductionCompanies>
                    val movieDetail = MovieDetail(
                        result.response.adult,
                        result.response.backdropPath,
                        result.response.budget,
                        genres,
                        result.response.homepage,
                        result.response.id,
                        result.response.imdbId,
                        result.response.originalLanguage,
                        result.response.originalTitle,
                        result.response.overview,
                        result.response.popularity,
                        result.response.posterPath,
                        productionCompanies,
                        result.response.releaseDate,
                        result.response.revenue,
                        result.response.runtime,
                        result.response.status,
                        result.response.tagline,
                        result.response.title,
                        result.response.video,
                        result.response.voteAverage,
                        result.response.voteCount
                    )
                    emit(Resource.Success(listOf(movieDetail)))
                }
                is ApiResultWrapper.Error -> {
                    val code: Int? = result.code
                    val failedType: String? = result.failedType
                    val message: String? = result.message
                    emit(Resource.Error(code, failedType, message))
                }
                is ApiResultWrapper.NetworkError -> {
                    val failedType: String? = result.failedType
                    val message: String? = result.message
                    emit(Resource.NetworkError(failedType, message))
                }
            }
        }
    }

    override fun getMovieByGenre(apiKey: String, genreId: String) =
        remoteDataSource.getMovieByGenre(apiKey, genreId)
}