package com.practicum.playlist_maker.domain.api

import com.practicum.playlist_maker.domain.TracksSearchState

interface TracksInteractor {
    fun interface TracksConsumer {
        fun consume(tracksSearchState: TracksSearchState)
    }

    fun searchTracks(expression: String, consumer: TracksConsumer)
}
