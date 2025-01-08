package com.practicum.playlist_maker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Delete(entity = PlaylistEntity::class)
    fun deletePlaylistEntity(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT *  FROM playlist_table WHERE id = :playlistId")
    fun getPlaylist(playlistId: Int): PlaylistEntity?

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun findPlaylistById(playlistId: Int): PlaylistEntity?

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

    @Query("DELETE FROM playlists_and_tracks_table WHERE playlistId = :playlistId")
    fun deletePlaylistsTracks(playlistId: Int)

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

    //** Убрать трек из плейлиста */
    @Transaction
    fun removeFromPlaylist(trackEntity: TracksInPlaylistEntity, playlistId: Int) {
        deleteFromPlaylist(
            PlaylistsAndTracksEntity(
                playlistId = playlistId,
                trackId = trackEntity.trackId
            )
        )
        deleteTrackSafety(trackEntity)
    }

    @Transaction
    fun deletePlaylistSafety(playlistEntity: PlaylistEntity) {
        deletePlaylistsTracks(playlistEntity.id)
        deletePlaylistEntity(playlistEntity)
    }

    @Query("SELECT * FROM tracks_in_playlist_table WHERE trackId = :trackId")
    fun findTrackById(trackId: Int): TracksInPlaylistEntity?

    @Delete(entity = TracksInPlaylistEntity::class)
    fun deleteTrack(trackEntity: TracksInPlaylistEntity)

    @Transaction
    fun deleteTrackByIdSafety(trackId: Int) {

        if (getPlaylistTracksId(trackId).isEmpty()) {

            val entity = findTrackById(trackId)
            if (entity != null) {
                deleteTrack(entity)
            }
        }
    }

    @Query(
        "SELECT * " +
                "FROM playlists_and_tracks_table " +
                "   LEFT JOIN tracks_in_playlist_table " +
                "       ON playlists_and_tracks_table.trackId = tracks_in_playlist_table.trackId  " +
                "WHERE playlists_and_tracks_table.playlistId = :playlistId " +
                "ORDER BY playlists_and_tracks_table.createDateTime DESC"
    )
    fun getTracks(playlistId: Int): List<TracksInPlaylistEntity>

    @Delete(entity = PlaylistsAndTracksEntity::class)
    fun deleteFromPlaylist(entity: PlaylistsAndTracksEntity)

    @Transaction
    fun deleteTrackSafety(entity: TracksInPlaylistEntity) {

        if (getPlaylistTracksId(entity.trackId).isEmpty()) {
            deleteTrack(entity)
        }
    }

}