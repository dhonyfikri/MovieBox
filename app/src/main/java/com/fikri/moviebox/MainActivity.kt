package com.fikri.moviebox

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikri.moviebox.core.data.Resource
import com.fikri.moviebox.core.data.source.remote.network.Token
import com.fikri.moviebox.core.domain.model.Genre
import com.fikri.moviebox.core.domain.model.Movie
import com.fikri.moviebox.core.helper.DimensManipulation
import com.fikri.moviebox.core.ui.adapter.FixedMovieListAdapter
import com.fikri.moviebox.core.ui.adapter.GenreListAdapter
import com.fikri.moviebox.databinding.ActivityMainBinding
import com.fikri.moviebox.view_model.MainVewModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainVewModel by viewModel()

    private lateinit var listGenre: ArrayList<Genre>
    private lateinit var listMovie: ArrayList<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
    }

    private fun setupData() {
        binding.header.tvHeaderTitle.text = "Story Box"

        binding.rvPopularMovie.setHasFixedSize(true)
        binding.rvPopularMovie.layoutManager = LinearLayoutManager(this)
        binding.rvGenre.setHasFixedSize(true)
        binding.rvGenre.layoutManager = FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.STRETCH
        }

        binding.ibGenreToggle.setOnClickListener {
            toggleGenreTab()
        }

        viewModel.isInitialObserveFlow.observe(this) {
            if (!it) {
                getAllGenre()
                getPopularMovie()
                viewModel.setFinishInitialObserveFlow()
            }
        }

        binding.srlSwipeRefeshHome.setOnRefreshListener {
            binding.srlSwipeRefeshHome.isRefreshing = false
            getAllGenre()
            getPopularMovie()
        }

        viewModel.listGenre.observe(this) {
            listGenre = it
            setGenreList(it)
        }
        viewModel.listMovie.observe(this) {
            listMovie = it
            setPopularMovieList(it)
        }
    }

    private fun setPopularMovieList(movieList: ArrayList<Movie>) {
        val adapterPopularMovie = FixedMovieListAdapter(movieList)
        binding.rvPopularMovie.adapter = adapterPopularMovie

        adapterPopularMovie.setOnItemClickCallback(object :
            FixedMovieListAdapter.OnItemClickCallback {
            override fun onClickedItem(data: Movie, posterView: View) {
                val genresOfMovie: ArrayList<Genre> = arrayListOf()
                listGenre.forEach {
                    if (data.genreIds.contains(it.id)) {
                        genresOfMovie.add(it)
                    }
                }
                val moveToMovieDetail = Intent(this@MainActivity, MovieDetailActivity::class.java)
                moveToMovieDetail.putExtra(MovieDetailActivity.EXTRA_MOVIE, data)
                moveToMovieDetail.putExtra(MovieDetailActivity.EXTRA_GENRE, genresOfMovie)
                startActivity(
                    moveToMovieDetail, ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@MainActivity, Pair(posterView, "poster_image_view")
                    ).toBundle()
                )
            }
        })
    }

    private fun setGenreList(genreList: ArrayList<Genre>) {
        val adapterGenre = GenreListAdapter(genreList)
        binding.rvGenre.adapter = adapterGenre

        adapterGenre.setOnItemClickCallback(object :
            GenreListAdapter.OnItemClickCallback {
            override fun onClickedItem(data: Genre) {
                val moveToGenreDiscover =
                    Intent(this@MainActivity, GenreDiscoverActivity::class.java)
                moveToGenreDiscover.putExtra(GenreDiscoverActivity.EXTRA_SELECTED_GENRE, data)
                moveToGenreDiscover.putExtra(
                    GenreDiscoverActivity.EXTRA_LIST_OF_GENRE,
                    listGenre
                )
                startActivity(moveToGenreDiscover)
            }
        })
    }

    fun getPopularMovie() {
        binding.apply {
            viewModel.getPopularMovie(Token.TMDB_TOKEN_V3).observe(this@MainActivity) { result ->
                when (result) {
                    is Resource.Loading -> {
                        rlLoadingPopularMovie.visibility = View.VISIBLE
                        rlPopularMovieMessage.visibility = View.GONE
                        rvPopularMovie.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        val listMovie = result.data as ArrayList<Movie>
                        viewModel.setListMovie(listMovie)

                        rlLoadingPopularMovie.visibility = View.GONE
                        rlPopularMovieMessage.visibility = View.GONE
                        rvPopularMovie.visibility = View.VISIBLE
                        if (listMovie.size == 0) {
                            rlPopularMovieMessage.visibility = View.VISIBLE
                            tvPopularMovieMessage.text = "No Popular Movie Data"
                        }
                    }
                    is Resource.Error -> {
                        rlLoadingPopularMovie.visibility = View.GONE
                        rlPopularMovieMessage.visibility = View.VISIBLE
                        tvPopularMovieMessage.text = result.message
                        rvPopularMovie.visibility = View.GONE
                    }
                    is Resource.NetworkError -> {
                        rlLoadingPopularMovie.visibility = View.GONE
                        rlPopularMovieMessage.visibility = View.VISIBLE
                        tvPopularMovieMessage.text = "Connection Failed"
                        rvPopularMovie.visibility = View.GONE
                    }
                }
            }

        }
    }

    fun getAllGenre() {
        binding.apply {
            viewModel.getAllGenre(Token.TMDB_TOKEN_V3).observe(this@MainActivity) { result ->
                when (result) {
                    is Resource.Loading -> {
                        rlLoadingGenre.visibility = View.VISIBLE
                        rlGenreMessage.visibility = View.GONE
                        rvGenre.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        val listGenre = result.data as ArrayList<Genre>
                        viewModel.setListGenre(listGenre)

                        rlLoadingGenre.visibility = View.GONE
                        rlGenreMessage.visibility = View.GONE
                        rvGenre.visibility = View.VISIBLE
                        if (listGenre.size == 0) {
                            rlPopularMovieMessage.visibility = View.VISIBLE
                            tvPopularMovieMessage.text = "No Movie Genre Data"
                        }
                    }
                    is Resource.Error -> {
                        rlLoadingGenre.visibility = View.GONE
                        rlGenreMessage.visibility = View.VISIBLE
                        tvGenreMessage.text = result.message
                        rvGenre.visibility = View.GONE
                    }
                    is Resource.NetworkError -> {
                        rlLoadingGenre.visibility = View.GONE
                        rlGenreMessage.visibility = View.VISIBLE
                        tvGenreMessage.text = "Connection Failed"
                        rvGenre.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun toggleGenreTab() {
        viewModel.isGenreTabCollapsed = !viewModel.isGenreTabCollapsed
        val params = binding.llGenreTab.layoutParams
        if (viewModel.isGenreTabCollapsed) {
            params.height = DimensManipulation.dpToPx(this, 140f).toInt()
            binding.ibGenreToggle.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_down
                )
            )
        } else {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            binding.ibGenreToggle.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_up
                )
            )
        }
        binding.llGenreTab.layoutParams = params
    }
}


//is Resource.Loading -> {
//    loadingModal.showLoadingModal(
//        this@LoginActivity,
//        LoadingModal.TYPE_GENERAL,
//        resources.getString(R.string.message_on_login)
//    )
//}
//is Resource.Success -> {
//    viewModel.apply {
//        saveSession((result.data[0] as Session).token)
//        saveUser((result.data[1] as User))
//        startActivity(
//            Intent(
//                this@LoginActivity,
//                HomeBottomNavigationActivity::class.java
//            )
//        )
//        finish()
//    }
//}
//is Resource.Error -> {
//    viewModel.apply {
//        responseType = result.failedType.toString()
//        responseMessage = result.code.toString() + " | " + result.message
//        setResponseModal(true)
//    }
//}
//is Resource.NetworkError -> {
//    viewModel.apply {
//        responseType = result.failedType.toString()
//        responseMessage = result.message
//        setResponseModal(true)
//    }
//}