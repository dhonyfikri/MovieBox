package com.fikri.moviebox.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AuthorReviewDetailsResponse(
    @SerializedName("name") var name: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("avatar_path") var avatarPath: String? = null,
    @SerializedName("rating") var rating: Float? = null
)
