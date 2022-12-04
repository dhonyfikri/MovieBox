package com.fikri.moviebox.core.data

import com.fikri.moviebox.core.data.source.remote.RemoteDataSource
import com.fikri.moviebox.core.data.source.remote.response.ApiResultWrapper
import com.fikri.moviebox.core.domain.model.MovieVideo
import com.fikri.moviebox.core.domain.repository_interface.IVideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VideoRepository(private val remoteDataSource: RemoteDataSource) : IVideoRepository {
    override fun getVideoTrailer(apiKey: String, movieId: Int): Flow<Resource<MovieVideo>> {
        return flow {
            emit(Resource.Loading())
            when (val result = remoteDataSource.getVideoTrailer(apiKey, movieId)) {
                is ApiResultWrapper.Success -> {
                    val listMovieVideo: ArrayList<MovieVideo> = arrayListOf()
                    result.response.results.map {
                        if (it.type?.lowercase() == "trailer") {
                            listMovieVideo.add(
                                MovieVideo(
                                    it.iso6391,
                                    it.iso31661,
                                    it.name,
                                    it.key,
                                    it.site,
                                    it.size,
                                    it.type,
                                    it.official,
                                    it.publishedAt,
                                    it.id
                                )
                            )
                        }
                    }
                    emit(Resource.Success(listMovieVideo))
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