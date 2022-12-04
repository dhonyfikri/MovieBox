package com.fikri.moviebox

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikri.moviebox.core.domain.model.Genre
import com.fikri.moviebox.core.domain.model.Movie
import com.fikri.moviebox.core.ui.adapter.EndlessMovieListAdapter
import com.fikri.moviebox.core.ui.adapter.LoadingStateAdapter
import com.fikri.moviebox.databinding.ActivityGenreDiscoverBinding
import com.fikri.moviebox.view_model.GenreDiscoverViewModel

class GenreDiscoverActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_SELECTED_GENRE = "extra_selected_genre"
        const val EXTRA_LIST_OF_GENRE = "extra_list_of_genre"
    }

    private lateinit var binding: ActivityGenreDiscoverBinding

    private lateinit var viewModel: GenreDiscoverViewModel
    private lateinit var adapter: EndlessMovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreDiscoverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = EndlessMovieListAdapter()

        viewModel = ViewModelProvider(
            this,
            GenreDiscoverViewModelFactory()
        )[GenreDiscoverViewModel::class.java]

        binding.apply {
            header.tvHeaderTitle.text = "Discover of ..."
            header.ibBack.setOnClickListener {
                finish()
            }
        }

        if (intent.getParcelableExtra<Genre>(EXTRA_SELECTED_GENRE) != null) {
            viewModel.selectedGenre =
                intent.getParcelableExtra<Genre>(EXTRA_SELECTED_GENRE) as Genre
            val genreName = viewModel.selectedGenre?.name ?: ""
            binding.header.tvHeaderTitle.text = "Discover of ${genreName}"
        }
        if (intent.getParcelableArrayListExtra<Genre>(EXTRA_LIST_OF_GENRE) != null) {
            viewModel.listOfGenre =
                intent.getParcelableArrayListExtra<Genre>(EXTRA_LIST_OF_GENRE) as ArrayList<Genre>
        }

        getEndlessMovieByGenre()
    }

    private fun getEndlessMovieByGenre() {
        viewModel.apply {
            binding.apply {
                rvMovie.layoutManager = LinearLayoutManager(this@GenreDiscoverActivity)
                rvMovie.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter(this@GenreDiscoverActivity) {
                        adapter.retry()
                    }
                )
                getMovieByGenre().observe(this@GenreDiscoverActivity) { data ->
                    adapter.submitData(lifecycle, data)
                }

                adapter.setOnItemClickCallback(object :
                    EndlessMovieListAdapter.OnItemClickCallback {
                    override fun onClickedItem(data: Movie, posterView: View) {
                        val genresOfMovie: ArrayList<Genre> = arrayListOf()
                        listOfGenre.forEach {
                            if (data.genreIds.contains(it.id)) {
                                genresOfMovie.add(it)
                            }
                        }
                        val moveToMovieDetail =
                            Intent(this@GenreDiscoverActivity, MovieDetailActivity::class.java)
                        moveToMovieDetail.putExtra(MovieDetailActivity.EXTRA_MOVIE, data)
                        moveToMovieDetail.putExtra(MovieDetailActivity.EXTRA_GENRE, genresOfMovie)
                        startActivity(
                            moveToMovieDetail, ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this@GenreDiscoverActivity, Pair(posterView, "poster_image_view")
                            ).toBundle()
                        )
                    }
                })
            }
        }
    }
}