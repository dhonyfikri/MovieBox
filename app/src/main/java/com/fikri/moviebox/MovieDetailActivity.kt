package com.fikri.moviebox

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikri.moviebox.core.data.Resource
import com.fikri.moviebox.core.data.source.remote.network.Server
import com.fikri.moviebox.core.data.source.remote.network.Token
import com.fikri.moviebox.core.domain.model.*
import com.fikri.moviebox.core.ui.adapter.FixedReviewListAdapter
import com.fikri.moviebox.core.ui.adapter.GenreListOfMovieAdapter
import com.fikri.moviebox.core.ui.adapter.ProductionCompaniesAdapter
import com.fikri.moviebox.databinding.ActivityMovieDetailBinding
import com.fikri.moviebox.view_model.MovieDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_GENRE = "extra_genre"
    }

    private lateinit var binding: ActivityMovieDetailBinding

    private val viewModel: MovieDetailViewModel by viewModel()

    private var genreListOfMovie: ArrayList<Genre> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getParcelableArrayListExtra<Genre>(EXTRA_GENRE) != null) {
            genreListOfMovie =
                intent.getParcelableArrayListExtra<Genre>(EXTRA_GENRE) as ArrayList<Genre>
            setGenreListOfMovie(genreListOfMovie)
        }

        setupData()
    }

    private fun setupData() {
        binding.rvGenre.setHasFixedSize(true)
        binding.rvGenre.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvCompanies.setHasFixedSize(true)
        binding.rvCompanies.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvReview.setHasFixedSize(true)
        binding.rvReview.layoutManager = LinearLayoutManager(this)

        binding.apply {
            header.tvHeaderTitle.text = "Movie Detail"
            header.ibBack.setOnClickListener {
                finish()
            }
        }

        if (intent.getParcelableExtra<Movie>(EXTRA_MOVIE) != null) {
            viewModel.selectedMovie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE) as Movie

            Glide.with(this@MovieDetailActivity)
                .load(Server.TMDB_IMAGE_BASE_URL + viewModel.selectedMovie.backdropPath)
                .error(R.drawable.default_image)
                .into(binding.ivBackdrop)

            Glide.with(this@MovieDetailActivity)
                .load(Server.TMDB_IMAGE_BASE_URL + viewModel.selectedMovie.posterPath)
                .error(R.drawable.default_image)
                .into(binding.ivPoster)

            binding.apply {
                tvTitle.text = viewModel.selectedMovie.title
                tvRating.text =
                    "${viewModel.selectedMovie.voteAverage} ( ${viewModel.selectedMovie.voteCount}  of vote )"
                tvOverview.text = viewModel.selectedMovie.overview
            }

            viewModel.isInitialObserveFlow.observe(this) {
                if (!it) {
                    getDetailMovie()
                    getReview()
                    getVideoTrailer()
                    viewModel.setFinishInitialObserveFlow()
                }
            }

            binding.srlSwipeRefeshDetailMovie.setOnRefreshListener {
                binding.srlSwipeRefeshDetailMovie.isRefreshing = false
                getDetailMovie()
                getReview()
                getVideoTrailer()
            }

            viewModel.movieDetail.observe(this) { detailMovie ->
                binding.apply {
                    tvDuration.text = "${detailMovie.runtime} Minutes"
                    tvTagline.text = detailMovie.tagline
                    btnWebsite.setOnClickListener {
                        if (detailMovie.homepage == null || (detailMovie.homepage
                                ?: "").isEmpty()
                        ) {
                            Toast.makeText(
                                this@MovieDetailActivity,
                                "Website unavailable",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(detailMovie.homepage)
                                )
                            )
                        }
                    }
                }
                setProductionCompaniesList(detailMovie.productionCompanies)
            }

            viewModel.listReview.observe(this) { listReview ->
                binding.btnMoreReview.setOnClickListener {
                    val moveToReviewList =
                        Intent(this@MovieDetailActivity, MovieReviewActivity::class.java)
                    moveToReviewList.putExtra(
                        MovieReviewActivity.EXTRA_SELECTED_MOVIE,
                        viewModel.selectedMovie
                    )
                    startActivity(moveToReviewList)
                }
                setTopReview(listReview)
            }

            viewModel.listMovieVideo.observe(this) { listVideo ->
                binding.btnTrailer.setOnClickListener {
                    if (listVideo.size > 0) {
                        val moveToMovieTrailer =
                            Intent(this@MovieDetailActivity, TrailerActivity::class.java)
                        moveToMovieTrailer.putExtra(
                            TrailerActivity.EXTRA_YOUTUBE_VIDEO,
                            listVideo[0]
                        )
                        startActivity(moveToMovieTrailer)
                    } else {
                        Toast.makeText(
                            this@MovieDetailActivity,
                            "Trailer unavailable",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        } else {
            Glide.with(this@MovieDetailActivity)
                .load(R.drawable.default_image)
                .into(binding.ivBackdrop)
            Glide.with(this@MovieDetailActivity)
                .load(R.drawable.default_image)
                .into(binding.ivPoster)
        }
    }

    private fun setGenreListOfMovie(listGenre: ArrayList<Genre>) {
        val adapterGenreListOfMovie = GenreListOfMovieAdapter(listGenre)
        binding.rvGenre.adapter = adapterGenreListOfMovie
    }

    private fun setProductionCompaniesList(listCompany: ArrayList<ProductionCompanies>) {
        val adapterProductionCompanies = ProductionCompaniesAdapter(listCompany)
        binding.rvCompanies.adapter = adapterProductionCompanies
    }

    private fun setTopReview(reviewList: ArrayList<Review>) {
        if (reviewList.size > 3) {
            binding.btnMoreReview.visibility = View.VISIBLE
        } else {
            binding.btnMoreReview.visibility = View.GONE
        }
        val limitedReviewList: ArrayList<Review> = arrayListOf()
        reviewList.map {
            if (limitedReviewList.size < 3) {
                limitedReviewList.add(it)
            }
        }
        val adapterTopReview = FixedReviewListAdapter(limitedReviewList)
        binding.rvReview.adapter = adapterTopReview

        adapterTopReview.setOnItemClickCallback(object :
            FixedReviewListAdapter.OnItemClickCallback {
            override fun onClickedItem(data: Review) {
                val moveToReviewDetail =
                    Intent(this@MovieDetailActivity, ReviewDetailActivity::class.java)
                moveToReviewDetail.putExtra(ReviewDetailActivity.EXTRA_REVIEW, data)
                startActivity(moveToReviewDetail)
            }
        })
    }

    fun getDetailMovie() {
        binding.apply {
            viewModel.getDetailMovie(Token.TMDB_TOKEN_V3, viewModel.selectedMovie.id ?: 0)
                .observe(this@MovieDetailActivity) { result ->
                    when (result) {
                        is Resource.Loading -> {
                            rlLoadingCompany.visibility = View.VISIBLE
                            rlCompanyMessage.visibility = View.GONE
                            rvCompanies.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            val movieDetail = result.data[0]
                            viewModel.setMovieDetailValue(movieDetail)

                            rlLoadingCompany.visibility = View.GONE
                            rlCompanyMessage.visibility = View.GONE
                            rvCompanies.visibility = View.VISIBLE
                            if (movieDetail.productionCompanies.size == 0) {
                                rlCompanyMessage.visibility = View.VISIBLE
                                tvCompanyMessage.text = "No Data of Company Production"
                            }
                        }
                        is Resource.Error -> {
                            rlLoadingCompany.visibility = View.GONE
                            rlCompanyMessage.visibility = View.VISIBLE
                            tvCompanyMessage.text = result.message
                            rvCompanies.visibility = View.GONE
                        }
                        is Resource.NetworkError -> {
                            rlLoadingCompany.visibility = View.GONE
                            rlCompanyMessage.visibility = View.VISIBLE
                            tvCompanyMessage.text = "Connection Failed"
                            rvCompanies.visibility = View.GONE
                        }
                    }
                }
        }
    }

    fun getReview() {
        binding.apply {
            viewModel.getReview(Token.TMDB_TOKEN_V3, viewModel.selectedMovie.id ?: 0)
                .observe(this@MovieDetailActivity) { result ->
                    when (result) {
                        is Resource.Loading -> {
                            rlLoadingReview.visibility = View.VISIBLE
                            rlReviewMessage.visibility = View.GONE
                            rvReview.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            val listReview = result.data as ArrayList<Review>
                            viewModel.setListReviewValue(listReview)

                            rlLoadingReview.visibility = View.GONE
                            rlReviewMessage.visibility = View.GONE
                            rvReview.visibility = View.VISIBLE
                            if (listReview.size == 0) {
                                rlReviewMessage.visibility = View.VISIBLE
                                tvReviewMessage.text = "No Data of Movie Review"
                            }
                        }
                        is Resource.Error -> {
                            rlLoadingReview.visibility = View.GONE
                            rlReviewMessage.visibility = View.VISIBLE
                            tvReviewMessage.text = result.message
                            rvReview.visibility = View.GONE
                        }
                        is Resource.NetworkError -> {
                            rlLoadingReview.visibility = View.GONE
                            rlReviewMessage.visibility = View.VISIBLE
                            tvReviewMessage.text = "Connection Failed"
                            rvReview.visibility = View.GONE
                        }
                    }
                }
        }
    }

    fun getVideoTrailer() {
        viewModel.getVideoTrailer(Token.TMDB_TOKEN_V3, viewModel.selectedMovie.id ?: 0)
            .observe(this) { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Nothing and no UI
                    }
                    is Resource.Success -> {
                        val listVideo = result.data as ArrayList<MovieVideo>
                        viewModel.setListMovieVideoValue(listVideo)
                    }
                    is Resource.Error -> {
                        // Nothing and no UI
                    }
                    is Resource.NetworkError -> {
                        // Nothing and no UI
                    }
                }
            }
    }
}