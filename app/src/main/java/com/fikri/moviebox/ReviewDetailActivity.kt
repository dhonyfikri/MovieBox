package com.fikri.moviebox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fikri.moviebox.core.data.source.remote.network.Server
import com.fikri.moviebox.core.domain.model.Review
import com.fikri.moviebox.core.helper.DateManipulation.withDateFormat
import com.fikri.moviebox.databinding.ActivityReviewDetailBinding
import java.text.DateFormat

class ReviewDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_REVIEW = "extra_review"
    }

    private lateinit var binding: ActivityReviewDetailBinding

    private lateinit var review: Review

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            header.tvHeaderTitle.text = "Review Detail"
            header.ibBack.setOnClickListener {
                finish()
            }
        }

        if (intent.getParcelableExtra<Review>(EXTRA_REVIEW) != null) {
            review = intent.getParcelableExtra<Review>(EXTRA_REVIEW) as Review

            val splitedAvatarPath = review.authorDetails?.avatarPath?.split("/")
            var avatarPath = ""
            if (splitedAvatarPath != null) {
                if (splitedAvatarPath.size > 2) {
                    for (i in 1..(splitedAvatarPath.size - 1)) {
                        avatarPath += splitedAvatarPath[i]
                        if (i < splitedAvatarPath.size - 1) {
                            avatarPath += "/"
                        }
                    }
                } else {
                    avatarPath = Server.TMDB_IMAGE_BASE_URL + review.authorDetails?.avatarPath
                }
            }

            Glide.with(this@ReviewDetailActivity)
                .load(avatarPath)
                .error(R.drawable.default_image)
                .into(binding.ivAvatar)
            binding.apply {
                tvUserName.text = review.author
                tvCreatedDate.text =
                    "Created at ${review.createdAt?.withDateFormat(type = DateFormat.FULL)}"
                tvUpdatedDate.text = "Updated at ${review.updatedAt?.withDateFormat(type = DateFormat.FULL)}"
                tvRating.text = review.authorDetails?.rating.toString()
                tvReview.text = review.content
            }
        } else {
            Glide.with(this@ReviewDetailActivity)
                .load(R.drawable.default_image)
                .into(binding.ivAvatar)
        }
    }
}