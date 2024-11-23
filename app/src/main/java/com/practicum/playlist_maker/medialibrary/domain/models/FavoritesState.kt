package com.practicum.playlist_maker.medialibrary.domain.models

sealed interface  FavoritesState {
    data class IsFavorites(val isFavorite: Boolean ) : FavoritesState
}