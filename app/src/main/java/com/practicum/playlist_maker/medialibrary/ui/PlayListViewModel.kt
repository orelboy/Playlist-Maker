package com.practicum.playlist_maker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PlayListViewModel (private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

        private val playlistStateLiveData = MutableLiveData<PlaylistViewState>()
        fun playlistStateObserver(): LiveData<PlaylistViewState> = playlistStateLiveData

        fun getPlaylists() {

            renderState(PlaylistViewState.Loading)
            viewModelScope.launch {
                playlistsInteractor.getAllPlaylists()
                    .flowOn(Dispatchers.IO)
                    .collect { playlists ->
                        if (playlists.isEmpty()) {
                            renderState(PlaylistViewState.Empty)
                        } else {
                            renderState(PlaylistViewState.Content(playlists))
                        }
                    }
            }
        }

        private fun renderState(state: PlaylistViewState) {
            playlistStateLiveData.postValue(state)
        }
}