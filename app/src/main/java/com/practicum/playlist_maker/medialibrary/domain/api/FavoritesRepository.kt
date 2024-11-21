package com.practicum.playlist_maker.medialibrary.domain.api

import com.practicum.playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun addTrackFavourite(track: Track)
    fun deleteTrackFavourite(track: Track)
    fun getTracksFavourite(): Flow<List<Track>>
    fun getAllTracksIdFavourite(): Flow<List<Int>>
}