package com.practicum.playlist_maker.search.domain.api

import com.practicum.playlist_maker.search.domain.models.SearchViewState
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
   fun searchTracks(expression: String): Flow<SearchViewState>
}