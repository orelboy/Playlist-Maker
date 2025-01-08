package com.practicum.playlist_maker.medialibrary.data

import com.google.gson.Gson
import com.practicum.playlist_maker.medialibrary.data.db.AppDatabase
import com.practicum.playlist_maker.medialibrary.data.db.PlaylistDbMapper
import com.practicum.playlist_maker.medialibrary.data.db.TrackDbMapper
import com.practicum.playlist_maker.medialibrary.domain.api.PlaylistsRepository
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistCreateData
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

    override suspend fun createPlaylist(data: PlaylistCreateData): Flow<Playlist> = flow {

        val path = saveImage(data)
        val playlistEntity = playlistDbMapper.map(data, path)

        appDatabase.playlistDao().insertPlaylist(playlistEntity)

        emit(playlistDbMapper.map(playlistEntity, fileStorage.getImageUri(path)))

    }.flowOn(Dispatchers.IO)

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> = flow {

        val playlistList = appDatabase.playlistDao().getAllPlaylists() .map { entity ->
            playlistDbMapper.map(
                entity,
                fileStorage.getImageUri(entity.coverLocalPath))
        }

        emit(playlistList)

    }.flowOn(Dispatchers.IO)

    private suspend fun saveImage(data: PlaylistCreateData): String {

        if (data.coverPathUri == null) return ""

        val fileName = data.name.filterNot { it.isWhitespace() }.plus(System.currentTimeMillis())
        val path = fileStorage.saveImage(name = fileName, uri = data.coverPathUri)
        return path

    }

//    private suspend fun getPlaylistByPlaylistEntity(playlistEntity: PlaylistEntity): Playlist {
//        return playlistDbMapper.map(
//            playlistEntity,
//            fileStorage.getImageUri(playlistEntity.coverLocalPath)
//        )
//    }

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

    override suspend fun deletePlaylist(playlistId: Int): Flow<Boolean> = flow {
        val playlistEntity = appDatabase.playlistDao().findPlaylistById(playlistId)
        playlistEntity?.let { entity ->

            val tracks = appDatabase.playlistDao().getPlaylistTracksId(playlistId = playlistId)

            appDatabase.playlistDao().deletePlaylistSafety(entity)
            tracks.forEach { id -> appDatabase.playlistDao().deleteTrackByIdSafety(id) }

            deleteImage(entity.coverLocalPath)

            emit(true)

        }

    }.flowOn(Dispatchers.IO)

    override suspend fun savePlaylistData(data: PlaylistEditData): Flow<Playlist> = flow {

        val playlistEntity = appDatabase.playlistDao().findPlaylistById(data.id)
        if (playlistEntity == null) {
            Throwable("PlaylistEntity not find by id = ${data.id}")
        }
        playlistEntity?.let { entity ->

            with(entity) {
                val lastName: String? = coverLocalPath
                coverLocalPath = saveImage(data)
                name = data.name
                description = data.description

                deleteImage(lastName)

            }

            appDatabase.playlistDao().insertPlaylist(entity)
            emit(playlistDbMapper.map(entity, fileStorage.getImageUri(entity.coverLocalPath)))

        }

    }.flowOn(Dispatchers.IO)

    override suspend fun getPlaylistById(playlistId: Int): Flow<Playlist?>  = flow {
        val result = appDatabase.playlistDao().getPlaylist(playlistId)

        emit(result?.let { playlistDbMapper.map(it, fileStorage.getImageUri(result.coverLocalPath)) })

    }.flowOn(Dispatchers.IO)


    override suspend fun removeTrack(track: Track, playlistId: Int): Flow<Boolean> = flow {
        appDatabase.playlistDao()
            .removeFromPlaylist(trackEntity = trackDbMapper.map(track), playlistId = playlistId)
        updatePlaylistInfoById(playlistId)
        emit(true)
    }.flowOn(Dispatchers.IO)

    override suspend fun getTracks(playlistId: Int): Flow<List<Track>> = flow {

        val result = appDatabase.playlistDao().getTracks(playlistId = playlistId)
            .map { entity -> trackDbMapper.map(entity) }

        emit(result)
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

//    fun getPlaylistByPlaylistEntity(playlistEntity: PlaylistEntity): Playlist {
//
//        return playlistDbMapper.map(
//            playlistEntity,
//            fileStorage.getImageUri(playlistEntity.coverLocalPath)
//        )
//    }

    private suspend fun deleteImage(path: String?) {

        if (path.isNullOrEmpty()) {
            fileStorage.deleteImage(path)
        }

    }

}