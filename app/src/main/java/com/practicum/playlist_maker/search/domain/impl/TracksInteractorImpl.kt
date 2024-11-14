package com.practicum.playlist_maker.search.domain.impl

import com.practicum.playlist_maker.search.domain.api.TracksInteractor
import com.practicum.playlist_maker.search.domain.api.TracksRepository
import com.practicum.playlist_maker.search.domain.models.SearchViewState
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(
    private val repository: TracksRepository,
) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<SearchViewState> {
            return repository.searchTracks(expression)
    }

}