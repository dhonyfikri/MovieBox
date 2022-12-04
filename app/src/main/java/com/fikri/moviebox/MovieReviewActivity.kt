package com.fikri.moviebox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikri.moviebox.core.domain.model.Movie
import com.fikri.moviebox.core.domain.model.Review
import com.fikri.moviebox.core.ui.adapter.EndlessReviewAdapter
import com.fikri.moviebox.core.ui.adapter.LoadingStateAdapter
import com.fikri.moviebox.databinding.ActivityMovieReviewBinding
import com.fikri.moviebox.view_model.MovieReviewViewModel

class MovieReviewActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SELECTED_MOVIE = "extra_selected_movie"
    }

    private lateinit var binding: ActivityMovieReviewBinding

    private lateinit var viewModel: MovieReviewViewModel
    private lateinit var adapter: EndlessReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = EndlessReviewAdapter()

        viewModel = ViewModelProvider(
            this,
            MovieReviewViewModelFactory()
        )[MovieReviewViewModel::class.java]

        binding.apply {
            header.tvHeaderTitle.text = "Review of ..."
            header.ibBack.setOnClickListener {
                finish()
            }
        }

        if (intent.getParcelableExtra<Movie>(EXTRA_SELECTED_MOVIE) != null) {
            viewModel.selectedMovie =
                intent.getParcelableExtra<Movie>(EXTRA_SELECTED_MOVIE) as Movie
            val movieTitle = viewModel.selectedMovie?.title ?: ""
            binding.header.tvHeaderTitle.text = "Review of ${movieTitle}"
        }

        getEndlessMovieReview()
    }

    private fun getEndlessMovieReview() {
        viewModel.apply {
            binding.apply {
                rvReview.layoutManager = LinearLayoutManager(this@MovieReviewActivity)
                rvReview.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter(this@MovieReviewActivity) {
                        adapter.retry()
                    }
                )
                getAllReviewList().observe(this@MovieReviewActivity) { data ->
                    adapter.submitData(lifecycle, data)
                }

                adapter.setOnItemClickCallback(object :
                    EndlessReviewAdapter.OnItemClickCallback {
                    override fun onClickedItem(data: Review) {
                        val moveToReviewDetail =
                            Intent(this@MovieReviewActivity, ReviewDetailActivity::class.java)
                        moveToReviewDetail.putExtra(ReviewDetailActivity.EXTRA_REVIEW, data)
                        startActivity(moveToReviewDetail)
                    }
                })
            }
        }
    }
}