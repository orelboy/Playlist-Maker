package com.practicum.playlist_maker.search.domain.api

import com.practicum.playlist_maker.search.domain.models.SearchViewState

interface TracksInteractor {
    fun interface TracksConsumer {
        fun consume(searchViewState: SearchViewState)
    }

    fun searchTracks(expression: String, consumer: TracksConsumer)
}
