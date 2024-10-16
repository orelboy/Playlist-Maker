package com.practicum.playlist_maker.search.domain.api

import com.practicum.playlist_maker.search.domain.models.Track

interface HistoryInteractor {
    fun getHistory(): ArrayList<Track>
    fun addTrackInHistory(track: Track)
    fun clearHistory()
}