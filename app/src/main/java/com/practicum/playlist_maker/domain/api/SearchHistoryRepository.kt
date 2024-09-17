package com.practicum.playlist_maker.domain.api

import com.practicum.playlist_maker.domain.models.Track

interface SearchHistoryRepository {
    fun getHistory(): ArrayList<Track>
    fun addTrackInHistory(track: Track)
    fun clearHistory()
}