package com.practicum.playlist_maker.medialibrary.domain.models

sealed interface PlaylistEditState {
    data object Loading : PlaylistEditState
    data object Empty: PlaylistEditState
    data class Create(val data: Playlist): PlaylistEditState
    data class Content(val data: PlaylistCreateData) : PlaylistEditState
}