package com.practicum.playlist_maker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.search.domain.api.HistoryInteractor
import com.practicum.playlist_maker.search.domain.api.TracksInteractor
import com.practicum.playlist_maker.search.domain.models.SearchViewState
import com.practicum.playlist_maker.search.domain.models.Track
import com.practicum.playlist_maker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel(){

    private val stateLiveData = MutableLiveData<SearchViewState>()
    fun observeState(): LiveData<SearchViewState> = stateLiveData

    private var lastSearchText: String? = null

    private val trackSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
        search(changedText)
    }

    fun searchDebounce(changedText: String) {
        if (lastSearchText != changedText) {
            lastSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    fun search(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchViewState.Loading)
            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(newSearchText.trim())
                    .collect{viewState ->
                        renderState(viewState)}
            }
        }
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        showHistory()
    }

    fun addToTrackHistory(track: Track) {
        historyInteractor.addTrackInHistory(track)
    }

    fun showHistory() {
        renderState(SearchViewState.History(historyInteractor.getHistory().toList()))
    }

    private fun renderState(state: SearchViewState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
