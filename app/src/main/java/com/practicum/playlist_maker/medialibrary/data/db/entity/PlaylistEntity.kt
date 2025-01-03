package com.practicum.playlist_maker.medialibrary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var description: String?,
    var coverLocalPath: String?,
    var tracksIds: String?,
    var tracksCount: Int?,
)