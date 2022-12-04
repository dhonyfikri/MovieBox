package com.fikri.moviebox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fikri.moviebox.core.domain.model.Movie
import com.fikri.moviebox.core.domain.model.MovieVideo
import com.fikri.moviebox.databinding.ActivityTrailerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class TrailerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_YOUTUBE_VIDEO = "extra_youtube_video"
    }

    private lateinit var binding: ActivityTrailerBinding

    private var movieVideo: MovieVideo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getParcelableExtra<MovieVideo>(EXTRA_YOUTUBE_VIDEO) != null) {
            movieVideo = intent.getParcelableExtra<MovieVideo>(EXTRA_YOUTUBE_VIDEO) as MovieVideo
            setupYoutubePlayer()
        }
    }

    private fun setupYoutubePlayer(){
        lifecycle.addObserver(binding.ytTrailer)

        binding.ytTrailer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.cueVideo(movieVideo?.key ?: "Unknown Key", 0f)
            }
        })
    }
}