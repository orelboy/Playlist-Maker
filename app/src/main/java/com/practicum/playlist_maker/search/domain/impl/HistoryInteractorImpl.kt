package com.practicum.playlist_maker.search.domain.impl

import com.practicum.playlist_maker.search.domain.api.HistoryInteractor
import com.practicum.playlist_maker.search.domain.api.HistoryRepository
import com.practicum.playlist_maker.search.domain.models.Track

class HistoryInteractorImpl(
    private val repository: HistoryRepository
) : HistoryInteractor {
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