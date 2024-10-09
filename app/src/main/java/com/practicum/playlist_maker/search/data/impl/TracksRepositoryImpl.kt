package com.practicum.playlist_maker.search.data.impl

import com.practicum.playlist_maker.search.data.network.NetworkClient
import com.practicum.playlist_maker.search.data.dto.SearchTracksRequest
import com.practicum.playlist_maker.search.data.dto.SearchTracksResponse
import com.practicum.playlist_maker.search.data.mapToTrack
import com.practicum.playlist_maker.search.domain.models.SearchViewState
import com.practicum.playlist_maker.search.domain.api.TracksRepository

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): SearchViewState {
        try {
            val response = networkClient.doRequest(SearchTracksRequest(expression))

            if (response.resultCode == 200) {
                val result = (response as SearchTracksResponse).results
                return if (result.isEmpty()) {
                    SearchViewState.Empty
                } else {
                    SearchViewState.Success((response).results.map {
                        it.mapToTrack()
                    })
                }
            } else return SearchViewState.Empty
        } catch (e: Exception) {
            return SearchViewState.Error
        }
    }
}