package com.practicum.playlist_maker.search.data.impl

import com.practicum.playlist_maker.search.data.network.NetworkClient
import com.practicum.playlist_maker.search.data.dto.SearchTracksRequest
import com.practicum.playlist_maker.search.data.dto.SearchTracksResponse
import com.practicum.playlist_maker.search.data.mapToTrack
import com.practicum.playlist_maker.search.domain.models.SearchViewState
import com.practicum.playlist_maker.search.domain.api.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository  {
    override fun searchTracks(expression: String): Flow<SearchViewState> = flow {
        val response = networkClient.doRequest(SearchTracksRequest(expression))
        when (response.resultCode) {
            200 ->{
                val result = (response as SearchTracksResponse).results
                if (result.isEmpty()){
                    emit(SearchViewState.Empty)
                }else{
                    emit(SearchViewState.Success((response).results.map {
                        it.mapToTrack()
                    }))
                }
            }
            400 ->{
                emit(SearchViewState.Empty)
            }else ->{
                emit(SearchViewState.Error)
            }
        }
    }
}