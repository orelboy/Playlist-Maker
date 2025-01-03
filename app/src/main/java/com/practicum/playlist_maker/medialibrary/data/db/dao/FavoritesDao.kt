package com.practicum.playlist_maker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlist_maker.medialibrary.data.db.entity.FavoritesEntity

@Dao
interface FavoritesDao {

    @Insert(entity = FavoritesEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrackFavorites(favoritesEntity: FavoritesEntity)

    @Delete(entity = FavoritesEntity::class)
    fun deleteTrackFavorites(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM favorites_table ORDER BY createDateTime DESC")
    fun getAllTracksFavorites(): List<FavoritesEntity>

    @Query("SELECT trackId FROM favorites_table")
    fun getAllTracksIdFavorites(): List<Int>

}