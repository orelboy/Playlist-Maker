package com.practicum.playlist_maker.search.data

import com.practicum.playlist_maker.search.data.dto.TrackDto
import com.practicum.playlist_maker.search.domain.models.Track

fun TrackDto.mapToTrack(): Track {
    return Track(
        trackId = this.trackId,
        artistName = this.artistName,
        trackName = this.trackName,
        trackTime = this.trackTime,
        artworkUrl100 = this.artworkUrl100,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl
    )
}
