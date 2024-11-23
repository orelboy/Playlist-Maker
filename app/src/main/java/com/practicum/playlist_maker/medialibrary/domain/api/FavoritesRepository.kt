package com.practicum.playlist_maker.medialibrary.domain.api

import com.practicum.playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun addTrackFavorites(track: Track)
    fun deleteTrackFavorites(track: Track)
    fun getTracksFavorites(): Flow<List<Track>>
    fun getAllTracksIdFavorites(): Flow<List<Int>>
}