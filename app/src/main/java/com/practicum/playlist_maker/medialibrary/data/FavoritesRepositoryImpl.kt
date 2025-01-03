package com.practicum.playlist_maker.medialibrary.data

import com.practicum.playlist_maker.medialibrary.data.db.AppDatabase
import com.practicum.playlist_maker.medialibrary.data.db.TrackDbMapper
import com.practicum.playlist_maker.medialibrary.domain.api.FavoritesRepository
import com.practicum.playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbMapper: TrackDbMapper,
): FavoritesRepository {
    override fun addTrackFavorites(track: Track) {
        val createDateTime = System.currentTimeMillis()
        val trackEntity = trackDbMapper.map(track, createDateTime)
        appDatabase.trackDao().insertTrackFavorites(trackEntity)
    }

    override fun deleteTrackFavorites(track: Track) {
        val createDateTime = System.currentTimeMillis()
        val tracksEntity = trackDbMapper.map(track, createDateTime)
        appDatabase.trackDao().deleteTrackFavorites (tracksEntity)
    }

    override fun getTracksFavorites(): Flow<List<Track>> = flow {

        val result = appDatabase.trackDao().getAllTracksFavorites()
            .map { trackDbMapper.map(it) }

        emit(result)
    }

    override fun getAllTracksIdFavorites(): Flow<List<Int>> = flow {
        val result = appDatabase.trackDao().getAllTracksIdFavorites()

        emit(result)
    }
}