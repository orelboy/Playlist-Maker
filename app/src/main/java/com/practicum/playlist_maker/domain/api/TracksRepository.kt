package com.practicum.playlist_maker.domain.api

import com.practicum.playlist_maker.domain.TracksSearchState

interface TracksRepository {
   fun searchTracks(expression: String): TracksSearchState
}