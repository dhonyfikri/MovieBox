package com.fikri.moviebox.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikri.moviebox.R
import com.fikri.moviebox.core.data.source.remote.network.Server
import com.fikri.moviebox.core.domain.model.Review
import com.fikri.moviebox.core.helper.DateManipulation.withDateFormat
import com.fikri.moviebox.databinding.ReviewItemBinding
import java.text.DateFormat

class EndlessReviewAdapter() :
    PagingDataAdapter<Review, EndlessReviewAdapter.ListViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(
                oldItem: Review,
                newItem: Review
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Review,
                newItem: Review
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: ReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            review: Review,
            onClicked: ((value: Review) -> Unit)? = null
        ) {
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

            binding.apply {
                Glide.with(itemView.context)
                    .load(avatarPath)
                    .error(R.drawable.default_image)
                    .into(ivAvatar)

                tvUserName.text = review.author
                tvReview.text = review.content
                tvCreatedDate.text =
                    "Created at ${review.createdAt?.withDateFormat(type = DateFormat.FULL)}"
                tvUpdatedDate.text =
                    "Updated at ${review.updatedAt?.withDateFormat(type = DateFormat.FULL)}"
                tvRating.text = review.authorDetails?.rating.toString()
                cvReviewItem.setOnClickListener {
                    onClicked?.invoke(
                        review
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val review = getItem(position)
        if (review != null) {
            holder.bind(review, onClicked = { value ->
                onItemClickCallback.onClickedItem(value)
            })
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onClickedItem(data: Review)
    }
}










