package com.practicum.playlist_maker.medialibrary.domain.api

import com.practicum.playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun getTracksFavorites(): Flow<List<Track>>
    suspend fun addTrackFavorites(track: Track)
    suspend fun deleteTrackFavorites(track: Track)
    suspend fun getAllTracksIdFavorites(): Flow<List<Int>>
}