package com.practicum.playlist_maker.medialibrary.domain.models

sealed interface PlaylistViewState {
    data object Loading : PlaylistViewState
    data class Content(val data: List<Playlist>) : PlaylistViewState
    data object Empty : PlaylistViewState
}