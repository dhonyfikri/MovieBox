package com.fikri.moviebox.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Review(
    var author: String? = null,
    var authorDetails: AuthorReviewDetails? = AuthorReviewDetails(),
    var content: String? = null,
    var createdAt: String? = null,
    var id: String? = null,
    var updatedAt: String? = null,
    var url: String? = null
) : Parcelable