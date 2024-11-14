package com.practicum.playlist_maker.search.domain.api

import com.practicum.playlist_maker.search.domain.models.SearchViewState
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
//    fun interface TracksConsumer {
//        fun consume(searchViewState: SearchViewState)
//    }

    fun searchTracks(expression: String): Flow<SearchViewState>
}
