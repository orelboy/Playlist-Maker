package com.practicum.playlist_maker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.practicum.playlist_maker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlist_maker.medialibrary.data.db.entity.PlaylistsAndTracksEntity
import com.practicum.playlist_maker.medialibrary.data.db.entity.TracksInPlaylistEntity

@Dao
interface PlaylistDao {
    // для таблицы PlaylistEntity
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT *  FROM playlist_table WHERE id = :playlistId")
    fun getPlaylist(playlistId: Int): PlaylistEntity?

    // для таблицы TracksInPlaylistEntity
    @Insert(entity = TracksInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertInTableTracksInPlaylist(tracksInPlaylistEntity: TracksInPlaylistEntity)

    // для таблицы PlaylistsAndTracksEntity
    @Insert(entity = PlaylistsAndTracksEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertInTablePlaylistsAndTracks(playlistsAndTracksEntity: PlaylistsAndTracksEntity)

    @Query("SELECT * FROM playlists_and_tracks_table WHERE trackId = :trackId AND playlistId = :playlistId")
    fun findTrackInPlaylist(trackId: Int, playlistId: Int): PlaylistsAndTracksEntity?

    @Query("SELECT trackId  FROM playlists_and_tracks_table WHERE playlistId = :playlistId")
    fun getPlaylistTracksId(playlistId: Int): List<Int>

    // Добавить трек в плейлист
    @Transaction
    fun addToPlaylist(trackEntity: TracksInPlaylistEntity, playlistId: Int) {
        insertInTableTracksInPlaylist(trackEntity)
        insertInTablePlaylistsAndTracks(
            PlaylistsAndTracksEntity(
                playlistId = playlistId,
                trackId = trackEntity.trackId,
                createDateTime = System.currentTimeMillis()
            )
        )
    }
}