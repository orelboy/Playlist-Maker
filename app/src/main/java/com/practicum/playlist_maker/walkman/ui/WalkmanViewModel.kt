package com.practicum.playlist_maker.walkman.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlist_maker.walkman.domain.api.WalkmanInteractor
import com.practicum.playlist_maker.walkman.domain.models.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class WalkmanViewModel(
    private val walkmanInteractor: WalkmanInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private var playerState = PlayerState.STATE_DEFAULT

    private val playStatusLiveData = MutableLiveData(playerState)
    fun observePlayStatusState(): LiveData<PlayerState> = playStatusLiveData

    private val currentPositionLiveData = MutableLiveData(DEFAULT_TIME)
    fun observeCurrentPositionState(): LiveData<String> = currentPositionLiveData

    fun preparePlayer(url: String?){
        if (url.isNullOrEmpty()) return
        walkmanInteractor.preparePlayer(url)
        walkmanInteractor.setOnPreparedListener { state ->
            playerState = state
            playStatusLiveData.value = playerState
        }
        walkmanInteractor.setOnCompletionListener { state ->
            playerState = state
            playStatusLiveData.value = playerState
            handler.removeCallbacks(updateTimer())
            currentPositionLiveData.value = DEFAULT_TIME
        }
    }

    fun playbackControl(){
        when(playStatusLiveData.value) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    fun startPlayer(){
        walkmanInteractor.startPlayer()
        playStatusLiveData.value = PlayerState.STATE_PLAYING
        handler.post(updateTimer())
    }

    fun pausePlayer(){
        walkmanInteractor.pausePlayer()
        playStatusLiveData.value = PlayerState.STATE_PAUSED
        handler.removeCallbacks(updateTimer())
    }

    private fun updateTimer(): Runnable {
        return object: Runnable{
            override fun run() {
                if (playStatusLiveData.value == PlayerState.STATE_PLAYING) {
                    val currentPosition = SimpleDateFormat(
                        DATE_FORMAT,
                        Locale.getDefault()
                    ).format(walkmanInteractor.getCurrentPosition())
                    currentPositionLiveData.value = currentPosition
                    handler.postDelayed(this, PLAY_TIME_DELAY)
                }
            }
        }
    }

    companion object {
        private const val PLAY_TIME_DELAY = 500L
        private const val DEFAULT_TIME = "00:00"
        private const val DATE_FORMAT = "mm:ss"
    }
}