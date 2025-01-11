package com.practicum.playlist_maker.medialibrary.domain.models

import com.practicum.playlist_maker.search.domain.models.Track

class PlaylistDetailedInfo(
    val id: Int,
    val name: String,
    val description: String?,
    val tracksCount: Int?,
    val coverPathUri: String?,
    val tracks: List<Track>,
) {

    var totalDuration: Long = 0L

    init {

        if (tracks.isNotEmpty()) {
            totalDuration = tracks.map { it.trackTime }.sum()
        }
    }

}