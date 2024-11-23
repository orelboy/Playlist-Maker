package com.practicum.playlist_maker.walkman.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlist_maker.medialibrary.domain.models.FavoritesState
import com.practicum.playlist_maker.search.domain.models.Track
import com.practicum.playlist_maker.utils.debounce
import com.practicum.playlist_maker.walkman.domain.api.WalkmanInteractor
import com.practicum.playlist_maker.walkman.domain.models.PlayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class WalkmanViewModel(
    private val walkmanInteractor: WalkmanInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    val track: Track?,
) : ViewModel() {
    private var timerJob: Job? = null
    private var currentTeack: Track? = track
    private var playerState = PlayerState.STATE_DEFAULT

    private val playStatusLiveData = MutableLiveData(playerState)
    fun observePlayStatusState(): LiveData<PlayerState> = playStatusLiveData

    private val currentPositionLiveData = MutableLiveData(DEFAULT_TIME)
    fun observeCurrentPositionState(): LiveData<String> = currentPositionLiveData

    private val isFavoriteLiveData = MutableLiveData<FavoritesState>()
    fun observeIsFavorite(): LiveData<FavoritesState> = isFavoriteLiveData

    private val isFavoriteChangeDebouce =
        debounce<Boolean>(delayMillis = 0, viewModelScope, false) { isFavorite ->
            setIsFavoriteValue(isFavorite)
        }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                favoritesInteractor.getAllTracksIdFavorites()
                    .collect { result ->
                        val isFavorite = result.contains(currentTeack?.trackId)
                        isFavoriteLiveData
                            .postValue(FavoritesState.IsFavorites(isFavorite))
                    }
            }
        }
    }

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
            currentPositionLiveData.value = DEFAULT_TIME
            timerJob?.cancel()
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

    private fun startPlayer(){
        walkmanInteractor.startPlayer()
        playStatusLiveData.value = PlayerState.STATE_PLAYING
        updateTimer()
    }

    fun pausePlayer(){
        walkmanInteractor.pausePlayer()
        playStatusLiveData.value = PlayerState.STATE_PAUSED
        timerJob?.cancel()
    }

    private fun updateTimer() {
        timerJob = viewModelScope.launch {
            while (playStatusLiveData.value == PlayerState.STATE_PLAYING) {
                delay(PLAY_TIME_DELAY)
                val currentPosition = SimpleDateFormat(
                    DATE_FORMAT,
                    Locale.getDefault()
                ).format(walkmanInteractor.getCurrentPosition())
                currentPositionLiveData.value = currentPosition
            }
        }
    }

    fun onFavoriteClicked() {
        isFavoriteChangeDebouce(!currentTeack!!.isFavorite)
    }

    private fun setIsFavoriteValue(isFavorite: Boolean) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (isFavorite) {
                    true -> currentTeack?.let { favoritesInteractor.addTrackFavorites(it) }
                    false -> currentTeack?.let { favoritesInteractor.deleteTrackFavorites(it) }
                }
                currentTeack?.isFavorite = isFavorite
                isFavoriteLiveData.postValue(FavoritesState.IsFavorites(isFavorite))
            }
        }

    }

    companion object {
        private const val PLAY_TIME_DELAY = 300L
        private const val DEFAULT_TIME = "00:00"
        private const val DATE_FORMAT = "mm:ss"
    }
}