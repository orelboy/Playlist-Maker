package com.practicum.playlist_maker.medialibrary.domain.impl

import com.practicum.playlist_maker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlist_maker.medialibrary.domain.api.FavoritesRepository
import com.practicum.playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
): FavoritesInteractor {
    override suspend fun getTracksFavourite(): Flow<List<Track>> {
        return favoritesRepository.getTracksFavourite()
    }
    override suspend fun addTrackFavourite(track: Track) {
        favoritesRepository.addTrackFavourite(track)
    }

    override suspend fun deleteTrackFavourite(track: Track) {
        favoritesRepository.deleteTrackFavourite(track)
    }

    override suspend fun getAllTracksIdFavourite(): Flow<List<Int>> {
        return favoritesRepository.getAllTracksIdFavourite()
    }
}