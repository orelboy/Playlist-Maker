package com.practicum.playlist_maker.presentation.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.practicum.playlist_maker.Creator
import com.practicum.playlist_maker.databinding.ActivitySearchBinding
import com.practicum.playlist_maker.domain.TracksSearchState
import com.practicum.playlist_maker.domain.models.Track
import com.practicum.playlist_maker.presentation.walkman.WalkmanActivity


private const val SEARCH_TEXT = "SEARCH_TEXT"

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private var searchText = ""
    private val tracks = ArrayList<Track>()
    private val adapter = SearchTracksAdapter()
    private var trackHistoryAdapter = SearchTracksAdapter()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val tracksInteractor by lazy { Creator.provideTrackInteractor() }
    private val searchHistoryInteractor by lazy { Creator.provideSearchHistoryInteractor(this) }
    private val searchRunnable = Runnable { search() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        adapter.tracks = tracks
        trackHistoryAdapter.tracks = searchHistoryInteractor.getHistory()

        fun closeKeyboard() {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            val view = this.currentFocus ?: View(this)
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

        binding.apply {
            rvTracks.adapter = adapter
            rvTracksHistory.adapter = trackHistoryAdapter

            searchEditText.requestFocus()
            searchHistoryScreen()

            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchDebounce()
                    showClearScreen()
                    searchHistoryScreen()
                }

                override fun afterTextChanged(s: Editable?) {
                    searchText = searchEditText.text.toString()
                }
            })

            back.setOnClickListener { finish() }
            updateButton.setOnClickListener { search() }


            searchEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    search()
                }
                false
            }

            iconClearInString.setOnClickListener {
                searchEditText.text.clear()
                closeKeyboard()
            }

            btnClearHistory.setOnClickListener {
                searchHistoryInteractor.clearHistory()
                searchHistory.visibility = View.GONE
            }

            adapter.setOnItemClickListener { track ->
                startWalkmanActivity(track)
            }

            trackHistoryAdapter.setOnItemClickListener { track ->
                startWalkmanActivity(track)
            }

            searchEditText.setOnFocusChangeListener { _, hasFocus ->
                searchHistory.visibility =
                    if (hasFocus &&
                        searchEditText.text.isEmpty() &&
                        trackHistoryAdapter.tracks.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        trackHistoryAdapter.notifyDataSetChanged()
    }


    private fun startWalkmanActivity(track: Track) {
        if (clickDebounce()) {
            searchHistoryInteractor.addTrackInHistory(track)
            trackHistoryAdapter.tracks = searchHistoryInteractor.getHistory()
            val walkmanIntent = Intent(this, WalkmanActivity::class.java)
            walkmanIntent.putExtra("TRACK_KEY", track)
            startActivity(walkmanIntent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, "")
        if (searchText.isNotEmpty()) {
            val editText = binding.searchEditText
            editText.setText(searchText)
            editText.setSelection(editText.length())
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun search() {
        if (binding.searchEditText.text.isNotEmpty()) {

            showLoading()

            tracksInteractor.searchTracks(
                binding.searchEditText.text.toString().trim()
            ) { tracksSearchState ->
                handler.post {
                    when (tracksSearchState) {
                        is TracksSearchState.Success -> {
                            showListTracks(tracksSearchState.tracks)
                        }

                        is TracksSearchState.EmptyListError -> {
                            showEmptyListError()
                        }

                        is TracksSearchState.NetworkError -> {
                            showNetworkError()
                        }
                    }
                }
            }
        }
    }
    private fun showListTracks(listTracks: List<Track>) {
        tracks.clear()
        tracks.addAll(listTracks)
        binding.apply {
            rvTracks.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private fun showLoading(){
        binding.apply {
            errorInternet.visibility = View.GONE
            errorSearch.visibility = View.GONE
            rvTracks.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showNetworkError() {
        binding.apply {
            errorInternet.visibility = View.VISIBLE
            errorSearch.visibility = View.GONE
            progressBar.visibility = View.GONE
            rvTracks.visibility = View.GONE
            rvTracks.removeAllViews()
        }
    }

    private fun showEmptyListError() {
        binding.apply {
            errorInternet.visibility = View.GONE
            errorSearch.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            rvTracks.visibility = View.GONE
            rvTracks.removeAllViews()
        }
    }

    private fun showClearScreen() {
        binding.apply {
            iconClearInString.isVisible = searchEditText.text.isNotEmpty()
            if (!iconClearInString.isVisible) {
                errorInternet.visibility = View.GONE
                errorSearch.visibility = View.GONE
                rvTracks.visibility = View.GONE
            }
        }
    }

    private fun searchHistoryScreen() {
        binding.apply {
            searchHistory.visibility =
                if (searchEditText.text.isEmpty() && trackHistoryAdapter.tracks.isNotEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            if (searchHistory.visibility == View.VISIBLE) {
                rvTracksHistory.removeAllViews()
            }
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}