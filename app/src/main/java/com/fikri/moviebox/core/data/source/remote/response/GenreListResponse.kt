package com.fikri.moviebox.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GenreListResponse(
    @SerializedName("genres") var genres: ArrayList<GenresResponse> = arrayListOf()
)

data class GenresResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null
)
