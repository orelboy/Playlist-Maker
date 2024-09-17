package com.practicum.playlist_maker.domain.impl

import com.practicum.playlist_maker.domain.api.SearchHistoryInteractor
import com.practicum.playlist_maker.domain.api.SearchHistoryRepository
import com.practicum.playlist_maker.domain.models.Track

class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : SearchHistoryInteractor {
    override fun getHistory(): ArrayList<Track> {
        return repository.getHistory()
    }

    override fun addTrackInHistory(track: Track) {
        repository.addTrackInHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}