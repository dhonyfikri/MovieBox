package com.fikri.moviebox.core.ui.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikri.moviebox.R
import com.fikri.moviebox.core.data.source.remote.network.Server
import com.fikri.moviebox.core.domain.model.Review
import com.fikri.moviebox.core.helper.DateManipulation.toDate
import com.fikri.moviebox.core.helper.DateManipulation.withDateFormat
import com.fikri.moviebox.databinding.ReviewItemBinding
import java.text.DateFormat

class FixedReviewListAdapter(private val listReview: ArrayList<Review>) :
    RecyclerView.Adapter<FixedReviewListAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: ReviewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val review = listReview[position]
        val splitedAvatarPath = review.authorDetails?.avatarPath?.split("/")
        var avatarPath = ""
        if (splitedAvatarPath != null) {
            if(splitedAvatarPath.size > 2) {
                for(i in 1..(splitedAvatarPath.size - 1)){
                    avatarPath += splitedAvatarPath[i]
                    if(i < splitedAvatarPath.size - 1){
                        avatarPath += "/"
                    }
                }
            } else {
                avatarPath = Server.TMDB_IMAGE_BASE_URL + review.authorDetails?.avatarPath
            }
        }

        Glide.with(holder.itemView.context)
            .load(avatarPath)
            .error(R.drawable.default_image)
            .into(holder.binding.ivAvatar)
        holder.apply {
            binding.apply {
                tvUserName.text = review.author
                tvReview.text = review.content
                tvCreatedDate.text =
                    "Created at ${review.createdAt?.withDateFormat(type = DateFormat.FULL)}"
                tvUpdatedDate.text = "Updated at ${review.updatedAt?.withDateFormat(type = DateFormat.FULL)}"
                tvRating.text = review.authorDetails?.rating.toString()
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onClickedItem(listReview[position])
        }
    }

    override fun getItemCount() = listReview.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onClickedItem(data: Review)
    }
}