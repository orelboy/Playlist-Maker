package com.practicum.playlist_maker.medialibrary.data.db.entity

import androidx.room.Entity

@Entity(tableName = "playlists_and_tracks_table", primaryKeys = ["playlistId", "trackId"])
data class PlaylistsAndTracksEntity (
    var playlistId: Int,
    var trackId: Int,
    val createDateTime: Long = 0 // для сортировки в будущем
)
