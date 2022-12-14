package com.fikri.moviebox.core.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikri.moviebox.R
import com.fikri.moviebox.core.data.source.remote.network.Server
import com.fikri.moviebox.core.domain.model.Movie
import com.fikri.moviebox.databinding.MovieItemBinding

class FixedMovieListAdapter(private val listMovie: ArrayList<Movie>) :
    RecyclerView.Adapter<FixedMovieListAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val movie = listMovie[position]
        Glide.with(holder.itemView.context)
            .load(Server.TMDB_IMAGE_BASE_URL + movie.posterPath)
            .error(R.drawable.default_image)
            .into(holder.binding.ivPoster)
        holder.apply {
            binding.apply {
                tvMovieTitle.text = movie.title
                tvOverview.text = movie.overview
                tvReleaseDate.text = "Release on ${movie.releaseDate}"
                tvRating.text = "${movie.voteAverage} ( ${movie.voteCount} of vote )"
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onClickedItem(listMovie[position], holder.binding.ivPoster)
        }
    }

    override fun getItemCount() = listMovie.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onClickedItem(data: Movie, posterView: View)
    }
}