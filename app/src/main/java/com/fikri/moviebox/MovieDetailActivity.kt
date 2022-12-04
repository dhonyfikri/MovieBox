package com.fikri.moviebox

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikri.moviebox.core.data.source.remote.network.ApiConfig
import com.fikri.moviebox.core.data.source.remote.network.Server
import com.fikri.moviebox.core.data.source.remote.network.Token
import com.fikri.moviebox.core.data.source.remote.response.MovieDetailResponse
import com.fikri.moviebox.core.data.source.remote.response.MovieVideoListResponse
import com.fikri.moviebox.core.data.source.remote.response.ReviewListResponse
import com.fikri.moviebox.core.domain.model.*
import com.fikri.moviebox.core.ui.adapter.FixedReviewListAdapter
import com.fikri.moviebox.core.ui.adapter.GenreListOfMovieAdapter
import com.fikri.moviebox.core.ui.adapter.ProductionCompaniesAdapter
import com.fikri.moviebox.databinding.ActivityMovieDetailBinding
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import retrofit2.awaitResponse
import java.io.IOException

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_GENRE = "extra_genre"
    }

    private lateinit var binding: ActivityMovieDetailBinding

    private lateinit var movie: Movie
    private var genreListOfMovie: ArrayList<Genre> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            movie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE) as Movie

            Glide.with(this@MovieDetailActivity)
                .load(Server.TMDB_IMAGE_BASE_URL + movie.backdropPath)
                .error(R.drawable.default_image)
                .into(binding.ivBackdrop)

            Glide.with(this@MovieDetailActivity)
                .load(Server.TMDB_IMAGE_BASE_URL + movie.posterPath)
                .error(R.drawable.default_image)
                .into(binding.ivPoster)

            binding.apply {
                tvTitle.text = movie.title
                tvRating.text = "${movie.voteAverage} ( ${movie.voteCount}  of vote )"
                tvOverview.text = movie.overview
            }

            lifecycleScope.launch {
                getDetailMovie(movie.id as Int)
                getTopReview(movie.id as Int)
                getVideoTrailer(movie.id as Int)
            }
        } else {
            Glide.with(this@MovieDetailActivity)
                .load(R.drawable.default_image)
                .into(binding.ivBackdrop)
            Glide.with(this@MovieDetailActivity)
                .load(R.drawable.default_image)
                .into(binding.ivPoster)
        }

        if (intent.getParcelableArrayListExtra<Genre>(EXTRA_GENRE) != null) {
            genreListOfMovie =
                intent.getParcelableArrayListExtra<Genre>(EXTRA_GENRE) as ArrayList<Genre>
            setGenreListOfMovie(genreListOfMovie)
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

    suspend fun getDetailMovie(movieId: Int) {
        val apiRequest = ApiConfig.getApiService()
            .getDetailMovie(apiKey = Token.TMDB_TOKEN_V3, movieId = movieId)

        try {
            val response: Response<MovieDetailResponse> = apiRequest.awaitResponse()
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("Berhasil:", responseBody.toString())
                val genres = responseBody?.genres?.map { Genre(it.id, it.name) } as ArrayList<Genre>
                val productionCompanies = responseBody.productionCompanies.map {
                    ProductionCompanies(
                        it.id,
                        it.logoPath,
                        it.name,
                        it.originCountry
                    )
                } as ArrayList<ProductionCompanies>
                val movieDetail = MovieDetail(
                    responseBody.adult,
                    responseBody.backdropPath,
                    responseBody.budget,
                    genres,
                    responseBody.homepage,
                    responseBody.id,
                    responseBody.imdbId,
                    responseBody.originalLanguage,
                    responseBody.originalTitle,
                    responseBody.overview,
                    responseBody.popularity,
                    responseBody.posterPath,
                    productionCompanies,
                    responseBody.releaseDate,
                    responseBody.revenue,
                    responseBody.runtime,
                    responseBody.status,
                    responseBody.tagline,
                    responseBody.title,
                    responseBody.video,
                    responseBody.voteAverage,
                    responseBody.voteCount
                )
                binding.apply {
                    tvDuration.text = "${movieDetail.runtime} Minutes"
                    tvTagline.text = movieDetail.tagline
                    btnWebsite.setOnClickListener {
                        if (movieDetail.homepage == null || (movieDetail.homepage
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
                                    Uri.parse(movieDetail.homepage)
                                )
                            )
                        }
                    }
                }
                setProductionCompaniesList(productionCompanies)
            } else {
                var errorMessage: String? = null
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage = jObjError.getString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Log.w("Error:", "${response.message()} | $errorMessage")
                Toast.makeText(this, "${response.message()} | $errorMessage", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: IOException) {
            Log.w("Network Error:", "Gak tau")
            Toast.makeText(this, "Koneksi Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun getTopReview(movieId: Int) {
        val apiRequest = ApiConfig.getApiService()
            .getListReview(apiKey = Token.TMDB_TOKEN_V3, movieId = movieId)

        try {
            val response: Response<ReviewListResponse> = apiRequest.awaitResponse()
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("Berhasil:", responseBody.toString())
                val listReview: ArrayList<Review> = arrayListOf()
                responseBody?.results?.map {
                    val authorDetails = AuthorReviewDetails(
                        it.authorDetails?.name,
                        it.authorDetails?.username,
                        it.authorDetails?.avatarPath,
                        it.authorDetails?.rating
                    )
                    listReview.add(
                        Review(
                            it.author,
                            authorDetails,
                            it.content,
                            it.createdAt,
                            it.id,
                            it.updatedAt,
                            it.url
                        )
                    )
                }
                setTopReview(listReview)
            } else {
                var errorMessage: String? = null
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage = jObjError.getString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Log.w("Error:", "${response.message()} | $errorMessage")
                Toast.makeText(this, "${response.message()} | $errorMessage", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: IOException) {
            Log.w("Network Error:", "Gak tau")
            Toast.makeText(this, "Koneksi Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun getVideoTrailer(movieId: Int) {
        val apiRequest = ApiConfig.getApiService()
            .getMovieVideo(apiKey = Token.TMDB_TOKEN_V3, movieId = movieId)

        try {
            val response: Response<MovieVideoListResponse> = apiRequest.awaitResponse()
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("Berhasil:", responseBody.toString())
                val listMovieVideo: ArrayList<MovieVideo> = arrayListOf()
                responseBody?.results?.map {
                    if (it.type?.lowercase() == "trailer") {
                        listMovieVideo.add(
                            MovieVideo(
                                it.iso6391,
                                it.iso31661,
                                it.name,
                                it.key,
                                it.site,
                                it.size,
                                it.type,
                                it.official,
                                it.publishedAt,
                                it.id
                            )
                        )
                    }
                }
                binding.btnTrailer.setOnClickListener {
                    if (listMovieVideo.size > 0) {
                        val moveToMovieTrailer =
                            Intent(this@MovieDetailActivity, TrailerActivity::class.java)
                        moveToMovieTrailer.putExtra(
                            TrailerActivity.EXTRA_YOUTUBE_VIDEO,
                            listMovieVideo[0]
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

            } else {
                var errorMessage: String? = null
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    errorMessage = jObjError.getString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Log.w("Error:", "${response.message()} | $errorMessage")
                Toast.makeText(this, "${response.message()} | $errorMessage", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: IOException) {
            Log.w("Network Error:", "Gak tau")
            Toast.makeText(this, "Koneksi Gagal", Toast.LENGTH_SHORT).show()
        }
    }
}