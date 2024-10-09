package com.practicum.playlist_maker.search.data.dto

data class SearchTracksResponse (
    val resultCount: Int,
    val results: List<TrackDto>
): Response()