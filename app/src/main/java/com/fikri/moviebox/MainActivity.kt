package com.fikri.moviebox

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikri.moviebox.core.data.source.remote.network.ApiConfig
import com.fikri.moviebox.core.data.source.remote.network.Token
import com.fikri.moviebox.core.data.source.remote.response.GenreListResponse
import com.fikri.moviebox.core.data.source.remote.response.MovieListResponse
import com.fikri.moviebox.core.domain.model.Genre
import com.fikri.moviebox.core.domain.model.Movie
import com.fikri.moviebox.core.helper.DimensManipulation
import com.fikri.moviebox.core.ui.adapter.FixedMovieListAdapter
import com.fikri.moviebox.core.ui.adapter.GenreListAdapter
import com.fikri.moviebox.databinding.ActivityMainBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import retrofit2.awaitResponse
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var isGenreTabCollapsed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.tvHeaderTitle.text = "Story Box"

        binding.rvPopularMovie.setHasFixedSize(true)
        binding.rvPopularMovie.layoutManager = LinearLayoutManager(this)
        binding.rvGenre.setHasFixedSize(true)
        binding.rvGenre.layoutManager = FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.STRETCH
        }

        lifecycleScope.launch {
            getAllGenre()
            getPopularMoview()
        }

        binding.ibGenreToggle.setOnClickListener {
            toggleGenreTab()
        }
    }

    private fun setPopularMovieList(movieList: ArrayList<Movie>) {
        val adapterPopularMovie = FixedMovieListAdapter(movieList)
        binding.rvPopularMovie.adapter = adapterPopularMovie

        adapterPopularMovie.setOnItemClickCallback(object :
            FixedMovieListAdapter.OnItemClickCallback {
            //            override fun onClickedItem(data: UserTail) {
//                val moveToUserDetail = Intent(this@ListUserActivity, UserDetailActivity::class.java)
//                moveToUserDetail.putExtra(UserDetailActivity.EXTRA_USER, data)
//                startActivity(moveToUserDetail)
//            }
            override fun onClickedItem(data: Movie) {
                Toast.makeText(this@MainActivity, data.title, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setGenreList(genreList: ArrayList<Genre>) {
        val adapterGenre = GenreListAdapter(genreList)
        binding.rvGenre.adapter = adapterGenre

        adapterGenre.setOnItemClickCallback(object :
            GenreListAdapter.OnItemClickCallback {
            override fun onClickedItem(data: Genre) {
                Toast.makeText(this@MainActivity, data.name, Toast.LENGTH_SHORT).show()
            }
        })
    }

    suspend fun getPopularMoview() {
        val apiRequest = ApiConfig.getApiService().getListMovie(apiKey = Token.TMDB_TOKEN_V3)

        try {
            val response: Response<MovieListResponse> = apiRequest.awaitResponse()
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("Berhasil:", responseBody.toString())
                val listMovie: ArrayList<Movie> = arrayListOf()
                responseBody?.let {
                    it.results.forEach { movieResponse ->
                        val movie = Movie(
                            movieResponse.adult,
                            movieResponse.backdropPath,
                            movieResponse.genreIds,
                            movieResponse.id,
                            movieResponse.originalLanguage,
                            movieResponse.originalTitle,
                            movieResponse.overview,
                            movieResponse.popularity,
                            movieResponse.posterPath,
                            movieResponse.releaseDate,
                            movieResponse.title,
                            movieResponse.video,
                            movieResponse.voteAverage,
                            movieResponse.voteCount
                        )
                        listMovie.add(movie)
                    }
                }
                setPopularMovieList(listMovie)
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

    suspend fun getAllGenre() {
        val apiRequest = ApiConfig.getApiService().getAllGenre(apiKey = Token.TMDB_TOKEN_V3)

        try {
            val response: Response<GenreListResponse> = apiRequest.awaitResponse()
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("Berhasil:", responseBody.toString())
                val listGenre: ArrayList<Genre> = arrayListOf()
                responseBody?.let {
                    it.genres.forEach { genreResponse ->
                        val genre = Genre(
                            genreResponse.id,
                            genreResponse.name
                        )
                        listGenre.add(genre)
                    }
                }
                setGenreList(listGenre)
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

    fun toggleGenreTab() {
        isGenreTabCollapsed = !isGenreTabCollapsed
        val params = binding.llGenreTab.layoutParams
        if (isGenreTabCollapsed) {
            params.height = DimensManipulation.dpToPx(this, 140f).toInt()
        } else {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        binding.llGenreTab.layoutParams = params
    }
}