package com.practicum.playlist_maker.medialibrary.data

import com.google.gson.Gson
import com.practicum.playlist_maker.medialibrary.data.db.AppDatabase
import com.practicum.playlist_maker.medialibrary.data.db.PlaylistDbMapper
import com.practicum.playlist_maker.medialibrary.data.db.TrackDbMapper
import com.practicum.playlist_maker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlist_maker.medialibrary.domain.api.PlaylistsRepository
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistEditData
import com.practicum.playlist_maker.medialibrary.domain.models.ResultAddingTrackInPlaylist
import com.practicum.playlist_maker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val fileStorage: FileStorage,
    private val playlistDbMapper: PlaylistDbMapper,
    private val trackDbMapper: TrackDbMapper,
    private val serializer: Gson,
    ): PlaylistsRepository {

    override suspend fun createPlaylist(data: PlaylistEditData): Flow<Playlist> = flow {

        val path = saveImage(data)
        val playlistEntity = playlistDbMapper.map(data, path)

        appDatabase.playlistDao().insertPlaylist(playlistEntity)

        emit(playlistDbMapper.map(playlistEntity, fileStorage.getImageUri(path)))

    }.flowOn(Dispatchers.IO)

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> = flow {

        val playlistList = appDatabase.playlistDao().getAllPlaylists().map { entity ->
            getPlaylistByPlaylistEntity(entity)
        }

        emit(playlistList)

    }.flowOn(Dispatchers.IO)

    private suspend fun saveImage(data: PlaylistEditData): String {

        if (data.coverPathUri == null) return ""

        val fileName = data.name.filterNot { it.isWhitespace() }.plus(System.currentTimeMillis())
        val path = fileStorage.saveImage(name = fileName, uri = data.coverPathUri)
        return path

    }

    private suspend fun getPlaylistByPlaylistEntity(playlistEntity: PlaylistEntity): Playlist {
        return playlistDbMapper.map(
            playlistEntity,
            fileStorage.getImageUri(playlistEntity.coverLocalPath)
        )
    }

    override suspend fun addTrack(
        track: Track,
        playlist: Playlist,
    ): Flow<ResultAddingTrackInPlaylist> = flow {
        val playlistTrack = appDatabase.playlistDao().findTrackInPlaylist(track.trackId, playlist.id)

        when {
            playlistTrack == null -> {
                appDatabase.playlistDao().addToPlaylist(trackDbMapper.map(track), playlist.id)
                updatePlaylistInfoById(playlist.id)
                emit(ResultAddingTrackInPlaylist.ADDED)
            }
            else -> emit(ResultAddingTrackInPlaylist.ADDED_EARLIER)
        }


    }.flowOn(Dispatchers.IO)

    private fun updatePlaylistInfoById(playlistId: Int) {
        appDatabase.playlistDao().getPlaylist(playlistId)?.let { entity ->
            val tracksId = appDatabase.playlistDao().getPlaylistTracksId(entity.id)

            if (entity.tracksCount != tracksId.size) {

                entity.tracksCount = tracksId.size
                entity.tracksIds = serializer.toJson(tracksId)

                appDatabase.playlistDao().insertPlaylist(entity)
            }
        }
    }

}