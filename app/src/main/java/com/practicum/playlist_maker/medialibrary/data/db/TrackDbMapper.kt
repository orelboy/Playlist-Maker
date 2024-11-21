package com.practicum.playlist_maker.medialibrary.data.db

import com.practicum.playlist_maker.medialibrary.data.db.entity.TrackEntity
import com.practicum.playlist_maker.search.domain.models.Track

class TrackDbMapper{
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            artistName = track.artistName,
            trackName = track.trackName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            createDateTime = System.currentTimeMillis()
        )
    }

    fun map(track: TrackEntity): Track {
        return  Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isFavorite = true
        )
    }


}