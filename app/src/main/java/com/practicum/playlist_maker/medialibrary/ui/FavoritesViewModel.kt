package com.practicum.playlist_maker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlist_maker.medialibrary.domain.models.FavoritesViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData<FavoritesViewState>()
    fun observeState(): LiveData<FavoritesViewState> = stateLiveData

    private var favoritesJob: Job? = null

    fun readFavoritesTracks() {
        favoritesJob?.cancel()
        favoritesJob = viewModelScope.launch {
            favoritesInteractor
                .getTracksFavorites()
                .flowOn(Dispatchers.Default)
                .collect { favoritesTracks ->
                    stateLiveData.postValue(
                        when {
                            favoritesTracks.isEmpty() -> FavoritesViewState.Empty
                            else -> FavoritesViewState.Content(favoritesTrackList = favoritesTracks)
                        }
                    )
                }

        }
    }

}