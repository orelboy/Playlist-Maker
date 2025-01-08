package com.practicum.playlist_maker.medialibrary.domain.models

sealed interface PlaylistItemShareState {
    data object Empty : PlaylistItemShareState
    data object None : PlaylistItemShareState
}