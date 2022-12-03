package com.fikri.moviebox.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("author") var author: String? = null,
    @SerializedName("author_details") var authorDetails: AuthorReviewDetailsResponse? = AuthorReviewDetailsResponse(),
    @SerializedName("content") var content: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("url") var url: String? = null
)