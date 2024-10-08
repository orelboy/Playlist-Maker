package com.practicum.playlist_maker.walkman.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.search.domain.models.Track
import com.practicum.playlist_maker.creator.AndroidUtils.dpToPx
import com.practicum.playlist_maker.databinding.ActivityWalkmanBinding
import com.practicum.playlist_maker.walkman.domain.models.PlayerState


class WalkmanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalkmanBinding
    private lateinit var viewModel: WalkmanViewModel

    private var track: Track? = null
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkmanBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        IntentCompat.getSerializableExtra(intent, "TRACK_KEY", Track::class.java)?.let { track = it }

        viewModel = ViewModelProvider(this, WalkmanViewModel.getViewModelFactory())[WalkmanViewModel::class.java]

        viewModel.observePlayStatusState().observe(this) {state ->
            when (state) {
                PlayerState.STATE_DEFAULT -> {viewModel.preparePlayer(url)}
                PlayerState.STATE_PREPARED -> preparePlayer()
                PlayerState.STATE_PLAYING -> startPlayer()
                PlayerState.STATE_PAUSED -> pausePlayer()
            }
        }
        viewModel.observeCurrentPositionState().observe(this){
            binding.playTime.text = it
        }

        fun getAlbumData(): String {
            return track?.collectionName ?: run {
                binding.album.isVisible = false
                binding.albumData.isVisible = false
                return ""
            }
        }

        binding.apply {
            backButton.setOnClickListener { finish() }
            playAndPause.setOnClickListener { viewModel.playbackControl() }
            trackName.text = track?.trackName
            artistName.text = track?.artistName
            durationData.text = track?.getTrackTime(track?.trackTime!!)
            albumData.text = getAlbumData()
            yearData.text = track?.releaseDate?.take(4)
            genreData.text = track?.primaryGenreName
            countryData.text = track?.country
            url = track?.previewUrl
        }

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
        viewModel.pausePlayer()
    }

    private fun preparePlayer() {
        binding.playAndPause.isEnabled = true
        binding.playAndPause.setImageResource(R.drawable.ic_button_play)
    }

    private fun startPlayer() {
        binding.playAndPause.setImageResource(R.drawable.ic_button_pause)
    }

    private fun pausePlayer() {
        binding.playAndPause.setImageResource(R.drawable.ic_button_play)
    }

}
