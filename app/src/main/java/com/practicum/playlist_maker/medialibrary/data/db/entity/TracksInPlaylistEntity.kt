package com.practicum.playlist_maker.medialibrary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tracks_in_playlist_table")
data class TracksInPlaylistEntity(
    @PrimaryKey
    val trackId: Int,
    val artistName: String,
    val trackName: String,
    val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
)