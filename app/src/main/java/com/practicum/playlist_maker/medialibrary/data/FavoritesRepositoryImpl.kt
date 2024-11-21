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
    override fun addTrackFavourite(track: Track) {
        val trackEntity = trackDbMapper.map(track)
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    override fun deleteTrackFavourite(track: Track) {
        val tracksEntity = trackDbMapper.map(track)
        appDatabase.trackDao().deleteTrack (tracksEntity)
    }

    override fun getTracksFavourite(): Flow<List<Track>> = flow {

        val result = appDatabase.trackDao().getAllTracks()
            .sortedByDescending { it.createDateTime }
            .map { trackDbMapper.map(it) }

        emit(result)
    }

    override fun getAllTracksIdFavourite(): Flow<List<Int>> = flow {
        val result = appDatabase.trackDao().getAllTracksId()

        emit(result)
    }
}