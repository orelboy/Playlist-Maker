package com.practicum.playlist_maker.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.recyclerView.Track
import com.practicum.playlist_maker.Utils.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale


class WalkmanActivity : AppCompatActivity() {

    private var track: Track? = null
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val PLAY_TIME_DELAY = 1000L
        private const val DEFAULT_TIME = "00:00"
    }
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var url: String? = null
    private var playOrPause: ImageView? = null
    private var handler: Handler? = null
    private var playTime: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkman)
        val backButton = findViewById<ImageView>(R.id.backButton)
        val imageAlbum = findViewById<ImageView>(R.id.imageAlbum)
        val trackName = findViewById<TextView>(R.id.trackName)
        val artistName = findViewById<TextView>(R.id.artistName)
        val durationData = findViewById<TextView>(R.id.durationData)
        val album = findViewById<TextView>(R.id.album)
        val albumData = findViewById<TextView>(R.id.albumData)
        val yearData = findViewById<TextView>(R.id.yearData)
        val genreData = findViewById<TextView>(R.id.genreData)
        val countryData = findViewById<TextView>(R.id.countryData)
        playOrPause = findViewById(R.id.playAndPause)
        playTime = findViewById(R.id.playTime)
        IntentCompat.getSerializableExtra(intent, "TRACK_KEY", Track::class.java)?.let { track = it }

        backButton?.setOnClickListener {
            finish()
        }

        imageAlbum?.let {
            Glide.with(this)
                .load(track?.getCoverArtwork())
                .placeholder(R.drawable.ic_placeholder_312)
                .fitCenter()
                .transform(RoundedCorners(8.dpToPx(this)))
                .into(it)
        }

        fun getAlbumData(): String {
            return track?.collectionName ?: run {
                album?.isVisible = false
                albumData?.isVisible = false
                return ""
            }
        }

        trackName?.text = track?.trackName
        artistName?.text = track?.artistName
        durationData?.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTime)
        albumData?.text = getAlbumData()
        yearData?.text = track?.releaseDate?.take(4)
        genreData?.text = track?.primaryGenreName
        countryData?.text = track?.country
        url = track?.previewUrl
        playTime?.text = DEFAULT_TIME
        handler = Handler(Looper.getMainLooper())
        preparePlayer()
        playOrPause?.setOnClickListener {
            playbackControl()
        }

    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler?.removeCallbacks(updateTimer())
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playOrPause?.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            handler?.removeCallbacks(updateTimer())
            playTime?.text = DEFAULT_TIME
            playOrPause?.setImageResource(R.drawable.ic_button_play)
        }
    }
    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playOrPause?.setImageResource(R.drawable.ic_button_pause)
        playerState = STATE_PLAYING
        handler?.post(updateTimer())
    }
    private fun pausePlayer() {
        mediaPlayer.pause()
        playOrPause?.setImageResource(R.drawable.ic_button_play)
        playerState = STATE_PAUSED
        handler?.removeCallbacks(updateTimer())
    }
    private fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    val currentPosition = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    playTime?.text = currentPosition
                    handler?.postDelayed(this, PLAY_TIME_DELAY)
                }
            }

        }
    }

}
