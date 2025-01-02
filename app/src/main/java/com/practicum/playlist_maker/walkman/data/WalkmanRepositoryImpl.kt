package com.practicum.playlist_maker.walkman.data

import android.media.MediaPlayer
import com.practicum.playlist_maker.walkman.domain.api.WalkmanRepository
import com.practicum.playlist_maker.walkman.domain.models.WalkmanState

class WalkmanRepositoryImpl( private val mediaPlayer: MediaPlayer):
    WalkmanRepository {
    override fun preparePlayer(url: String) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    override fun setOnPreparedListener(listener: (WalkmanState) -> Unit) {
        mediaPlayer.setOnPreparedListener {
            listener.invoke(WalkmanState.STATE_PREPARED)
        }
    }

    override fun setOnCompletionListener(listener: (WalkmanState) -> Unit) {
        mediaPlayer.setOnCompletionListener {
            listener.invoke(WalkmanState.STATE_PREPARED)
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