package com.practicum.playlist_maker.walkman.domain.models

import com.practicum.playlist_maker.medialibrary.domain.models.Playlist

sealed class WalkmanPlaylistViewState(val listState: Int) {
    class Loading(visibleState: Int) : WalkmanPlaylistViewState(visibleState)
    class Content(val data: List<Playlist>, listState: Int) : WalkmanPlaylistViewState(listState)
}