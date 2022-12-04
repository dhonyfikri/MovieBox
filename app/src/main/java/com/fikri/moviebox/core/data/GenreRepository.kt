package com.fikri.moviebox.core.data

import com.fikri.moviebox.core.data.source.remote.RemoteDataSource
import com.fikri.moviebox.core.data.source.remote.response.ApiResultWrapper
import com.fikri.moviebox.core.domain.model.Genre
import com.fikri.moviebox.core.domain.repository_interface.IGenreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GenreRepository(private val remoteDataSource: RemoteDataSource) : IGenreRepository {
    override fun getAllGenre(apiKey: String): Flow<Resource<Genre>> {
        return flow {
            emit(Resource.Loading())
            when (val result = remoteDataSource.getAllGenre(apiKey)) {
                is ApiResultWrapper.Success -> {
                    val listGenre: ArrayList<Genre> = arrayListOf()
                    result.response.genres.forEach {
                        val genre = Genre(it.id, it.name)
                        listGenre.add(genre)
                    }
                    emit(Resource.Success(listGenre))
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
}