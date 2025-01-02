package com.practicum.playlist_maker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlist_maker.medialibrary.data.db.dao.PlaylistDao
import com.practicum.playlist_maker.medialibrary.data.db.dao.FavoritesDao
import com.practicum.playlist_maker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlist_maker.medialibrary.data.db.entity.FavoritesEntity
import com.practicum.playlist_maker.medialibrary.data.db.entity.PlaylistsAndTracksEntity
import com.practicum.playlist_maker.medialibrary.data.db.entity.TracksInPlaylistEntity

@Database(
    version = 3,
    entities = [
        FavoritesEntity::class,
        PlaylistEntity::class,
        TracksInPlaylistEntity::class,
        PlaylistsAndTracksEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): FavoritesDao
    abstract fun playlistDao(): PlaylistDao
}