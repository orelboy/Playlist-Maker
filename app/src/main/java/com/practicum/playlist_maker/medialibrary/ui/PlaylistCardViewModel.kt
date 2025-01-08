package com.practicum.playlist_maker.medialibrary.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistCardState
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistDetailedInfo
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistItemShareState
import com.practicum.playlist_maker.search.domain.models.Track
import com.practicum.playlist_maker.sharing.domain.api.SharingInteractor
import kotlinx.coroutines.launch

class PlaylistCardViewModel(
    private val application: Application,
    private val playlistInteractor: PlaylistsInteractor,
    private val sharingInteractor: SharingInteractor
) :
    ViewModel() {

    private var lastPlaylistInfo: PlaylistDetailedInfo? = null

    private val stateLiveData = MutableLiveData<PlaylistCardState>()
    fun stateLiveDataObserver(): LiveData<PlaylistCardState> = stateLiveData

    private val shareStateLiveData = MutableLiveData<PlaylistItemShareState>()
    fun shareStateLiveDataObserver(): LiveData<PlaylistItemShareState> = shareStateLiveData

    fun updatePlaylistInfoById(playlistId: Int, forceMode: Boolean = false) {

        if (lastPlaylistInfo != null && !forceMode) return

        viewModelScope.launch {

            playlistInteractor.getPlaylistById(id = playlistId)
                .collect {

                    it?.let { playlist ->

                        playlistInteractor.getPlaylistTracks(playlistId = playlistId)
                            .collect {

                                val detailedInfo =
                                    getPlaylistDetaledInfo(playlist = playlist, tracks = it)
                                lastPlaylistInfo = detailedInfo

                                renderState(PlaylistCardState.Content(data = detailedInfo))

                            }

                    } ?: Throwable(message = "Playlist not founded by ID: $playlistId")

                }
        }

    }

    fun updatePlaylistInfoByLast() {
        lastPlaylistInfo?.let { updatePlaylistInfoById(it.id, forceMode = true) }
    }

    fun removeFromPlaylist(track: Track) {

        lastPlaylistInfo?.let { info ->

            viewModelScope.launch {
                playlistInteractor
                    .removeTrack(track = track, playlistId = info.id)
                    .collect {
                        updatePlaylistInfoById(playlistId = info.id, forceMode = true)
                    }
            }

        }

    }

    fun sharePlaylist() {
        lastPlaylistInfo?.let { info ->
            if (info.tracks.isEmpty()) {
                renderShareState(PlaylistItemShareState.Empty)
            } else {
                sharingInteractor.share(info.mapToString(application))
                finishShare()
            }
        }
    }

    fun getCommandsList() {
        lastPlaylistInfo?.let { info ->
            renderState(PlaylistCardState.Commands(data = info))
        }
    }

    fun deletePlaylist() {

        lastPlaylistInfo?.let { info ->

            viewModelScope.launch {
                playlistInteractor.deletePlaylist(info.id)
                    .collect { success ->
                        if (success) {
                            renderState(PlaylistCardState.Deleted)
                        }
                    }
            }

        }

    }

    private fun finishShare() {
        renderShareState(PlaylistItemShareState.None)
    }

    private fun getPlaylistDetaledInfo(
        playlist: Playlist,
        tracks: List<Track>,
    ): PlaylistDetailedInfo {
        return PlaylistDetailedInfo(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            tracksCount = playlist.tracksCount,
            coverPathUri = playlist.coverPathUri,
            tracks = tracks
        )
    }

    private fun renderState(state: PlaylistCardState) {
        stateLiveData.postValue(state)
    }

    private fun renderShareState(state: PlaylistItemShareState) {
        shareStateLiveData.postValue(state)
    }

}