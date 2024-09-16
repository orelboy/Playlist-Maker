package com.practicum.playlist_maker.data

import com.practicum.playlist_maker.data.dto.SearchTracksRequest
import com.practicum.playlist_maker.data.dto.SearchTracksResponse
import com.practicum.playlist_maker.domain.TracksSearchState
import com.practicum.playlist_maker.domain.api.TracksRepository

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): TracksSearchState {
        try {
            val response = networkClient.doRequest(SearchTracksRequest(expression))

            if (response.resultCode == 200) {
                val result = (response as SearchTracksResponse).results
                return if (result.isEmpty()) {
                    TracksSearchState.EmptyListError
                } else {
                    TracksSearchState.Success((response).results.map {
                        it.mapToTrack()
                    })
                }
            } else return TracksSearchState.EmptyListError
        } catch (e: Exception) {
            return TracksSearchState.NetworkError
        }
    }
}