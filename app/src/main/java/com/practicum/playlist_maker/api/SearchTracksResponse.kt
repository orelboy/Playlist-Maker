package com.practicum.playlist_maker.api

import com.practicum.playlist_maker.recyclerView.Track

data class SearchTracksResponse (
    val resultCount: Int,
    val results: List<Track>
)