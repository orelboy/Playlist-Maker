package com.practicum.playlist_maker.presentation.walkman

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker.Creator
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.domain.models.Track
import com.practicum.playlist_maker.presentation.AndroidUtils.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale
import com.practicum.playlist_maker.databinding.ActivityWalkmanBinding
import com.practicum.playlist_maker.domain.models.PlayerState


class WalkmanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalkmanBinding

    private var track: Track? = null
    private var playerState = PlayerState.STATE_DEFAULT
    private var url: String? = null
    private var playTime: TextView? = null
    private var handler: Handler = Handler(Looper.getMainLooper())
    private val interactor by lazy { Creator.provideWalkmanInteractor() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkmanBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        playTime = binding.playTime
        IntentCompat.getSerializableExtra(intent, "TRACK_KEY", Track::class.java)?.let { track = it }

        fun getAlbumData(): String {
            return track?.collectionName ?: run {
                binding.album.isVisible = false
                binding.albumData.isVisible = false
                return ""
            }
        }

        binding.apply {
            backButton.setOnClickListener { finish() }
            playAndPause.setOnClickListener { playbackControl() }
            trackName.text = track?.trackName
            artistName.text = track?.artistName
            durationData.text = track?.getTrackTime(track?.trackTime!!)
            albumData.text = getAlbumData()
            yearData.text = track?.releaseDate?.take(4)
            genreData.text = track?.primaryGenreName
            countryData.text = track?.country
            url = track?.previewUrl
            playTime.text = DEFAULT_TIME
        }

        preparePlayer()

        binding.imageAlbum.let {
            Glide.with(this)
                .load(track?.getCoverArtwork())
                .placeholder(R.drawable.ic_placeholder_312)
                .fitCenter()
                .transform(RoundedCorners(8.dpToPx(this)))
                .into(it)
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        interactor.getRelease()
        handler.removeCallbacks(updateTimer())
    }

    private fun preparePlayer() {
        if (url.isNullOrEmpty()) return
        interactor.preparePlayer(url!!)
        interactor.setOnPreparedListener { state ->
            playerState = state
            binding.playAndPause.isEnabled = true
        }
        interactor.setOnCompletionListener { state ->
            playerState = state
            handler.removeCallbacks(updateTimer())
            playTime?.text = DEFAULT_TIME
            binding.playAndPause.setImageResource(R.drawable.ic_button_play)
        }
    }
    private fun playbackControl() {
        when(playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }
    private fun startPlayer() {
        interactor.startPlayer()
        binding.playAndPause.setImageResource(R.drawable.ic_button_pause)
        playerState = PlayerState.STATE_PLAYING
        handler.post(updateTimer())
    }
    private fun pausePlayer() {
        interactor.pausePlayer()
        binding.playAndPause.setImageResource(R.drawable.ic_button_play)
        playerState = PlayerState.STATE_PAUSED
        handler.removeCallbacks(updateTimer())
    }
    private fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == PlayerState.STATE_PLAYING) {
                    val currentPosition = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(interactor.getCurrentPosition())
                    playTime?.text = currentPosition
                    handler.postDelayed(this, PLAY_TIME_DELAY)
                }
            }
        }
    }
    companion object {
        private const val PLAY_TIME_DELAY = 500L
        private const val DEFAULT_TIME = "00:00"
    }
}
