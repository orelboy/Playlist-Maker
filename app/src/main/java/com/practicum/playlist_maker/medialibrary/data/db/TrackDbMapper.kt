package com.practicum.playlist_maker.medialibrary.data.db

import com.practicum.playlist_maker.medialibrary.data.db.entity.FavoritesEntity
import com.practicum.playlist_maker.medialibrary.data.db.entity.TracksInPlaylistEntity
import com.practicum.playlist_maker.search.domain.models.Track

class TrackDbMapper{
    fun map(track: Track, createDateTime: Long): FavoritesEntity {
        return FavoritesEntity(
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
            createDateTime = createDateTime
        )
    }

    fun map(track: FavoritesEntity): Track {
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

    fun map(track: Track): TracksInPlaylistEntity {
        return TracksInPlaylistEntity(
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
        )
    }

    fun map(track: TracksInPlaylistEntity): Track {
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
            previewUrl = track.previewUrl
        )
    }

}