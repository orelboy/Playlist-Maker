package com.practicum.playlist_maker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlist_maker.medialibrary.data.db.dao.TrackDao
import com.practicum.playlist_maker.medialibrary.data.db.entity.TrackEntity

@Database(
    version = 2,
    entities = [TrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}