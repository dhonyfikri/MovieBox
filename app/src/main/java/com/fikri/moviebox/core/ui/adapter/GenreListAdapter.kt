package com.fikri.moviebox.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fikri.moviebox.core.domain.model.Genre
import com.fikri.moviebox.databinding.GenreItemBinding

class GenreListAdapter(private val listGenre: ArrayList<Genre>) :
    RecyclerView.Adapter<GenreListAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: GenreItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            GenreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val genre = listGenre[position]
        holder.binding.tvGenreName.text = genre.name

        holder.itemView.setOnClickListener {
            onItemClickCallback.onClickedItem(listGenre[position])
        }
    }

    override fun getItemCount() = listGenre.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onClickedItem(data: Genre)
    }
}


//class ListUserAdapter(private val listUserTail: ArrayList<UserTail>) :
//    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
//
//    private lateinit var onItemClickCallback: OnItemClickCallback
//
//    class ListViewHolder(var binding: UserListItemBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val binding =
//            UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ListViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        holder.binding.apply {
//            tvName.text = listUserTail[position].username
//            tvUserGithubLink.text = listUserTail[position].github_link
//        }
//        Glide.with(holder.itemView.context)
//            .load(listUserTail[position].avatar)
//            .error(R.drawable.default_user_image)
//            .circleCrop()
//            .into(holder.binding.ivAvatarThumbnail)
//
//        holder.itemView.setOnClickListener {
//            onItemClickCallback.onClickedItem(listUserTail[holder.adapterPosition])
//        }
//    }
//
//    override fun getItemCount(): Int = listUserTail.size
//
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }
//
//    interface OnItemClickCallback {
//        fun onClickedItem(data: UserTail)
//    }
//}