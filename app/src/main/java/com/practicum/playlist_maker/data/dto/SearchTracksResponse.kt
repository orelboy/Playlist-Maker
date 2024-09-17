package com.practicum.playlist_maker.data.dto

data class SearchTracksResponse (
    val resultCount: Int,
    val results: List<TrackDto>
): Response()