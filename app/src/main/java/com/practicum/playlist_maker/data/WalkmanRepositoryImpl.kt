package com.practicum.playlist_maker.data

import android.media.MediaPlayer
import com.practicum.playlist_maker.domain.api.WalkmanRepository
import com.practicum.playlist_maker.domain.models.PlayerState

class WalkmanRepositoryImpl( private val mediaPlayer: MediaPlayer = MediaPlayer() ): WalkmanRepository {
    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    override fun setOnPreparedListener(listener: (PlayerState) -> Unit) {
        mediaPlayer.setOnPreparedListener {
            listener.invoke(PlayerState.STATE_PREPARED)
        }
    }

    override fun setOnCompletionListener(listener: (PlayerState) -> Unit) {
        mediaPlayer.setOnCompletionListener {
            listener.invoke(PlayerState.STATE_PREPARED)
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getRelease() {
        mediaPlayer.release()
    }

}