package com.fikri.moviebox.core.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fikri.moviebox.core.data.source.remote.network.ApiService
import com.fikri.moviebox.core.domain.model.AuthorReviewDetails
import com.fikri.moviebox.core.domain.model.Review

class ReviewPagingSource(
    private val apiService: ApiService,
    private val apiKey: String,
    private val movieId: Int
) : PagingSource<Int, Review>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getEndlessListReview(
                apiKey = apiKey,
                page = position,
                movieId = movieId
            ).results

            val responseData: ArrayList<Review> = arrayListOf()
            response.forEach {
                val authorReviewDetails = AuthorReviewDetails(
                    it.authorDetails?.name,
                    it.authorDetails?.username,
                    it.authorDetails?.avatarPath,
                    it.authorDetails?.rating,
                )
                responseData.add(
                    Review(
                        it.author,
                        authorReviewDetails,
                        it.content,
                        it.createdAt,
                        it.id,
                        it.updatedAt,
                        it.url
                    )
                )
            }

            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}