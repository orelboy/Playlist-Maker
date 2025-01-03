package com.practicum.playlist_maker.walkman.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlist_maker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlist_maker.medialibrary.domain.models.FavoritesState
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistTrackAddState
import com.practicum.playlist_maker.medialibrary.domain.models.ResultAddingTrackInPlaylist
import com.practicum.playlist_maker.search.domain.models.Track
import com.practicum.playlist_maker.utils.debounce
import com.practicum.playlist_maker.walkman.domain.api.WalkmanInteractor
import com.practicum.playlist_maker.walkman.domain.models.WalkmanPlaylistViewState
import com.practicum.playlist_maker.walkman.domain.models.WalkmanState
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
    private val playlistsInteractor: PlaylistsInteractor,

    val track: Track?,
) : ViewModel() {
    private var timerJob: Job? = null
    private var currentTeack: Track? = track
    private var walkmanState = WalkmanState.STATE_DEFAULT

    private val playStatusLiveData = MutableLiveData(walkmanState)
    fun observePlayStatusState(): LiveData<WalkmanState> = playStatusLiveData

    private val currentPositionLiveData = MutableLiveData(DEFAULT_TIME)
    fun observeCurrentPositionState(): LiveData<String> = currentPositionLiveData

    private val isFavoriteLiveData = MutableLiveData<FavoritesState>()
    fun observeIsFavorite(): LiveData<FavoritesState> = isFavoriteLiveData

    private val isFavoriteChangeDebouce =
        debounce<Boolean>(delayMillis = 0, viewModelScope, false) { isFavorite ->
            setIsFavoriteValue(isFavorite)
        }

    private val playlistStateLiveData = MutableLiveData<WalkmanPlaylistViewState>()
    fun observePlaylistState(): LiveData<WalkmanPlaylistViewState> = playlistStateLiveData

    private val playlistTrackAddedState = MutableLiveData<PlaylistTrackAddState>()
    fun playlistTrackAddedStateObserver(): LiveData<PlaylistTrackAddState> = playlistTrackAddedState


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
            walkmanState = state
            playStatusLiveData.value = walkmanState
        }
        walkmanInteractor.setOnCompletionListener { state ->
            walkmanState = state
            playStatusLiveData.value = walkmanState
            currentPositionLiveData.value = DEFAULT_TIME
            timerJob?.cancel()
        }
    }

    fun playbackControl(){
        when(playStatusLiveData.value) {
            WalkmanState.STATE_PLAYING -> {
                pausePlayer()
            }
            WalkmanState.STATE_PREPARED, WalkmanState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    private fun startPlayer(){
        walkmanInteractor.startPlayer()
        playStatusLiveData.value = WalkmanState.STATE_PLAYING
        updateTimer()
    }

    fun pausePlayer(){
        walkmanInteractor.pausePlayer()
        playStatusLiveData.value = WalkmanState.STATE_PAUSED
        timerJob?.cancel()
    }

    private fun updateTimer() {
        timerJob = viewModelScope.launch {
            while (playStatusLiveData.value == WalkmanState.STATE_PLAYING) {
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
    fun getAllPlaylists(listState: Int) {
        viewModelScope.launch {
            playlistsInteractor.getAllPlaylists()
                .collect { playlists ->
                    renderState(
                        WalkmanPlaylistViewState.Content(
                            data = playlists,
                            listState = listState
                        )
                    )
                }
        }
    }

    fun clearPlaylists(listState: Int) {
        renderState(
            WalkmanPlaylistViewState.Content(
                data = emptyList(),
                listState = listState
            )
        )
    }

    fun addTrackToPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.addTrackInPlaylist(track!!, playlist)
                .collect { result ->
                    val state: PlaylistTrackAddState = when (result) {
                        ResultAddingTrackInPlaylist.ADDED -> PlaylistTrackAddState.TrackAdded(
                            track,
                            playlist
                        )

                        ResultAddingTrackInPlaylist.ADDED_EARLIER -> PlaylistTrackAddState.TrackAddedEarly(
                            track,
                            playlist
                        )

                        ResultAddingTrackInPlaylist.ERROR -> PlaylistTrackAddState.Error
                    }
                    renderPlaylistTrackAddedState(state)
                }
        }
    }

    fun clearPlaylistTrackAddedMessage() {
        renderPlaylistTrackAddedState(PlaylistTrackAddState.Empty)
    }

    private fun renderState(state: WalkmanPlaylistViewState) {
        playlistStateLiveData.postValue(state)
    }
    private fun renderPlaylistTrackAddedState(state: PlaylistTrackAddState) {
        playlistTrackAddedState.postValue(state)
    }

    companion object {
        private const val PLAY_TIME_DELAY = 300L
        private const val DEFAULT_TIME = "00:00"
        private const val DATE_FORMAT = "mm:ss"
    }
}