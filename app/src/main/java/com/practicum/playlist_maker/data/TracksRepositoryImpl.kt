package com.practicum.playlist_maker.data

import com.practicum.playlist_maker.data.dto.SearchTracksRequest
import com.practicum.playlist_maker.data.dto.SearchTracksResponse
import com.practicum.playlist_maker.domain.TracksSearchState
import com.practicum.playlist_maker.domain.api.TracksRepository
import com.practicum.playlist_maker.domain.models.Track

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
                        Track(
                            it.trackId,
                            it.artistName,
                            it.trackName,
                            it.trackTime,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    })
                }
            } else return TracksSearchState.EmptyListError
        } catch (e: Exception) {
            return TracksSearchState.NetworkError
        }
    }
}