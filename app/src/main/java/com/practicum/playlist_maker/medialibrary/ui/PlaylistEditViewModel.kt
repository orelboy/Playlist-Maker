package com.practicum.playlist_maker.medialibrary.ui

import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistEditData
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistEditState
import kotlinx.coroutines.launch

class PlaylistEditViewModel(
     playlistInteractor: PlaylistsInteractor
) :
    PlayListCreateViewModel(playlistInteractor) {

    private var playlistId: Int? = null

    private lateinit var playlistAtStart: Playlist

    fun fillByPlaylistId(id: Int) {
        playlistId = id
        viewModelScope.launch {
            playlistsInteractor.getPlaylistById(id)
                .collect {
                    it?.let { fillByPlaylist(it) }
                }

        }

    }

    override fun savePlaylist() {

        if (playlistAtStart.name == lastName
            && playlistAtStart.description == lastDescription
            && playlistAtStart.coverPathUri == lastCover
        ) {
            renderState(PlaylistEditState.Create(playlistAtStart))
        } else {

            renderState(PlaylistEditState.Create(playlistAtStart))

            playlistId?.let { id ->

                val info = PlaylistEditData(
                    id = id,
                    name = lastName,
                    description = lastDescription,
                    coverPathUri = lastCover
                )

                viewModelScope.launch {
                    playlistsInteractor.saveEditPlaylistInfo(info)
                        .collect {
                            renderState(PlaylistEditState.Create(it))
                        }
                }

            }
        }

    }


    private fun fillByPlaylist(playlist: Playlist) {
        lastName = playlist.name
        lastDescription = playlist.description
        lastCover = playlist.coverPathUri
        renderLastData()

        playlistAtStart = playlist
    }

}