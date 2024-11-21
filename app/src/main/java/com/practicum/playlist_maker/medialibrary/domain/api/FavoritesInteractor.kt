package com.practicum.playlist_maker.medialibrary.domain.api

import com.practicum.playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun getTracksFavourite(): Flow<List<Track>>
    suspend fun addTrackFavourite(track: Track)
    suspend fun deleteTrackFavourite(track: Track)
    suspend fun getAllTracksIdFavourite(): Flow<List<Int>>
}