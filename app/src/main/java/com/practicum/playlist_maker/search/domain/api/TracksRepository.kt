package com.practicum.playlist_maker.search.domain.api

import com.practicum.playlist_maker.search.domain.models.SearchViewState

interface TracksRepository {
   fun searchTracks(expression: String): SearchViewState
}