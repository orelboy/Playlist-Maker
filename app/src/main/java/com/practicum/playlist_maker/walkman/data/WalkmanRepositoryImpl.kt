package com.practicum.playlist_maker.walkman.data

import android.media.MediaPlayer
import com.practicum.playlist_maker.walkman.domain.api.WalkmanRepository
import com.practicum.playlist_maker.walkman.domain.models.PlayerState

class WalkmanRepositoryImpl( private val mediaPlayer: MediaPlayer):
    WalkmanRepository {
    override fun preparePlayer(url: String) {
        mediaPlayer.reset()
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