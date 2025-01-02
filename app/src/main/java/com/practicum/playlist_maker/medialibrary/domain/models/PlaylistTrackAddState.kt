package com.practicum.playlist_maker.medialibrary.domain.models

import com.practicum.playlist_maker.search.domain.models.Track

sealed interface PlaylistTrackAddState {
    data object Loading : PlaylistTrackAddState
    data class TrackAdded(val track: Track, val playlist: Playlist) : PlaylistTrackAddState
    data class TrackAddedEarly(val track: Track, val playlist: Playlist) : PlaylistTrackAddState
    data object Empty : PlaylistTrackAddState
    data object Error : PlaylistTrackAddState
}