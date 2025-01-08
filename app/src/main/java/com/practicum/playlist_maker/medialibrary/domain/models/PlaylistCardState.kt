package com.practicum.playlist_maker.medialibrary.domain.models

sealed interface PlaylistCardState {
    data object Loading : PlaylistCardState
    data class Content(val data: PlaylistDetailedInfo) : PlaylistCardState
    data class Commands(val data: PlaylistDetailedInfo) : PlaylistCardState
    data object Deleted : PlaylistCardState
}