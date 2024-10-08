package com.practicum.playlist_maker.search.domain.models


sealed interface SearchViewState {
    data class Success(val trackList: List<Track>) : SearchViewState
    data class History(val historyList: List<Track>) : SearchViewState
    data object Empty : SearchViewState
    data object Error : SearchViewState
    data object Loading : SearchViewState
}