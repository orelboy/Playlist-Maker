package com.practicum.playlist_maker.medialibrary.ui

import android.app.Application
import android.content.Context
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
import com.practicum.playlist_maker.utils.AndroidUtils.trackDurationToTimeString
import com.practicum.playlist_maker.utils.AndroidUtils.tracksCountString
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
                sharingInteractor.share(mapToString(application))
                finishShare()
            }
        }
    }

    private fun mapToString(context: Context): String {

        val builder = StringBuilder()
        builder.append(lastPlaylistInfo?.name)
        if (!lastPlaylistInfo?.description.isNullOrEmpty()) {
            builder.append("\n")
            builder.append(lastPlaylistInfo?.description)
        }
        builder.append("\n")
        builder.append(lastPlaylistInfo?.tracksCount?.let { tracksCountString(context, it) })


        lastPlaylistInfo?.tracks?.forEachIndexed { index, track ->
            builder.append("\n")
            builder.append(
                "${index + 1}.${track.artistName} - ${track.trackName} (${
                    trackDurationToTimeString(
                        track.trackTime
                    )
                })"
            )


        }

        return builder.toString()

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
            coverPathUri = playlist.coverPathUri?.toString(),
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