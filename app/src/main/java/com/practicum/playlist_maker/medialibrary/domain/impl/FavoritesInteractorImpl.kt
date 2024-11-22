package com.practicum.playlist_maker.medialibrary.domain.impl

import com.practicum.playlist_maker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlist_maker.medialibrary.domain.api.FavoritesRepository
import com.practicum.playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
): FavoritesInteractor {
    override suspend fun getTracksFavorites(): Flow<List<Track>> {
        return favoritesRepository.getTracksFavorites()
    }
    override suspend fun addTrackFavorites(track: Track) {
        favoritesRepository.addTrackFavorites(track)
    }

    override suspend fun deleteTrackFavorites(track: Track) {
        favoritesRepository.deleteTrackFavorites(track)
    }

    override suspend fun getAllTracksIdFavorites(): Flow<List<Int>> {
        return favoritesRepository.getAllTracksIdFavorites()
    }
}