package com.practicum.playlist_maker.medialibrary.domain.models

import com.practicum.playlist_maker.search.domain.models.Track

sealed interface FavoritesViewState {
    data object Empty : FavoritesViewState
    data class Content(val favoritesTrackList: List<Track> ) : FavoritesViewState
}