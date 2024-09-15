package com.practicum.playlist_maker.domain

import com.practicum.playlist_maker.domain.models.Track

sealed class TracksSearchState {
    class Success(val tracks: List<Track>) : TracksSearchState()
    data object NetworkError : TracksSearchState()
    data object EmptyListError : TracksSearchState()
}