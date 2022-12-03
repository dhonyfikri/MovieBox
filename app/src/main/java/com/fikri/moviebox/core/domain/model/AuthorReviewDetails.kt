package com.fikri.moviebox.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthorReviewDetails(
    var name: String? = null,
    var username: String? = null,
    var avatarPath: String? = null,
    var rating: Float? = null
) : Parcelable