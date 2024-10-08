package com.practicum.playlist_maker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist_maker.creator.Creator
import com.practicum.playlist_maker.search.domain.models.SearchViewState
import com.practicum.playlist_maker.search.domain.models.Track

class SearchViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val tracksInteractor = Creator.provideTrackInteractor()
    private val historyInteractor = Creator.provideSearchHistoryInteractor(application)
    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<SearchViewState>()
    fun observeState(): LiveData<SearchViewState> = stateLiveData


    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private var lastSearchText: String? = null

    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText) {
            return
        }

        this.lastSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { search(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }


    fun search(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchViewState.Loading)
            tracksInteractor.searchTracks(newSearchText.trim()) { viewState ->
                renderState(viewState)
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
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    this[APPLICATION_KEY] as Application
                )
            }
        }
    }
}
