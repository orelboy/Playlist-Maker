package com.practicum.playlist_maker.medialibrary.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistEditData
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistEditState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PlayListCreateViewModel (private val playlistsInteractor: PlaylistsInteractor) : ViewModel()  {

    private val playListState = MutableLiveData<PlaylistEditState>(PlaylistEditState.Empty)
    fun playListStateObserver(): LiveData<PlaylistEditState> = playListState

    private var lastName: String = ""
    private var lastDescription: String = ""
    private var lastCover: Uri? = null

    fun onNameChanged(newName: String) {

        if (newName == lastName) return

        lastName = newName
        renderLastData()

    }

    fun onDescriptionChanged(newDescription: String) {

        if (newDescription == lastDescription) return

        lastDescription = newDescription
        renderLastData()

    }

    fun onCoverChanged(newCover: Uri?) {
        if (newCover == lastCover) return

        lastCover = newCover
        renderLastData()

    }

    fun hasUnsavedModify(): Boolean {

        return lastName.isNotEmpty()
                || lastDescription.isNotEmpty()
                || lastCover != null

    }

    fun savePlaylist() {

        viewModelScope.launch {

            playlistsInteractor.createPlaylist(
                PlaylistEditData(
                    name = lastName,
                    description = lastDescription,
                    coverPathUri = lastCover
                )
            ).flowOn(Dispatchers.IO)
                .collect { playlist ->
                    renderState(PlaylistEditState.Create(playlist))
                }

        }

    }

    private fun renderLastData() {

        val state = if (lastName.isNotEmpty()
            || lastDescription.isNotEmpty()
            || lastCover != null
        ) PlaylistEditState.Content(
            PlaylistEditData(
                name = lastName,
                description = lastDescription,
                coverPathUri = lastCover
            )
        ) else PlaylistEditState.Empty

        renderState(state)

    }

    private fun renderState(state: PlaylistEditState) {
        playListState.postValue(state)
    }


}