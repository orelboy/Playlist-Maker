package com.practicum.playlist_maker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.FragmentSearchBinding
import com.practicum.playlist_maker.search.domain.models.SearchViewState
import com.practicum.playlist_maker.search.domain.models.Track
import com.practicum.playlist_maker.walkman.ui.WalkmanActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<SearchViewModel>()

    private var trackAdapter = SearchTracksAdapter()
    private var trackHistoryAdapter = SearchTracksAdapter()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.iconClearInString.isVisible = !s.isNullOrEmpty()
                viewModel.showHistory()
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher.let { binding.searchEditText.addTextChangedListener(it) }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        trackAdapter = SearchTracksAdapter { track ->
            viewModel.addToTrackHistory(track)
            startWalkmanActivity(track)
        }

        trackHistoryAdapter = SearchTracksAdapter { track ->
            viewModel.addToTrackHistory(track)
            startWalkmanActivity(track)
            viewModel.showHistory() //
        }

        binding.apply {
            rvTracks.adapter = trackAdapter
            rvTracksHistory.adapter = trackHistoryAdapter

            updateButton.setOnClickListener {
                viewModel.search(searchEditText.text.toString())
            }

            iconClearInString.setOnClickListener {
                searchEditText.text.clear()
                closeKeyboard()
                viewModel.showHistory()
            }

            btnClearHistory.setOnClickListener {
                viewModel.clearHistory()
            }
        }
    }

    private fun render(state: SearchViewState){
        when (state) {
            is SearchViewState.Success -> showListTracks(state.trackList)
            is SearchViewState.History -> searchHistoryScreen(state.historyList)
            is SearchViewState.Empty -> showEmptyListError()
            is SearchViewState.Loading -> showLoading()
            is SearchViewState.Error -> showNetworkError()
        }
    }

    private fun closeKeyboard() {
        val inputMethodManager = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity?.currentFocus ?: view
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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
    private fun showListTracks(listTracks: List<Track>) {
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(listTracks)
        binding.apply {
            rvTracks.isVisible = true
            progressBar.isVisible = false
            errorInternet.isVisible = false
            errorSearch.isVisible = false
            rvTracks.removeAllViews()
        }
    }

    private fun showLoading(){
        binding.apply {
            errorInternet.isVisible = false
            errorSearch.isVisible = false
            rvTracks.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showNetworkError() {
        binding.apply {
            errorInternet.isVisible = true
            errorSearch.isVisible = false
            progressBar.isVisible = false
            rvTracks.isVisible = false
        }
    }

    private fun showEmptyListError() {
        binding.apply {
            errorInternet.isVisible = false
            errorSearch.isVisible = true
            progressBar.isVisible = false
            rvTracks.isVisible = false
        }
    }

    private fun searchHistoryScreen(historyList: List<Track>) {
        trackHistoryAdapter.tracks.clear()
        trackHistoryAdapter.tracks.addAll(historyList)
        binding.apply {
            rvTracks.isVisible = false
            progressBar.isVisible = false
            errorInternet.isVisible = false
            errorSearch.isVisible = false
            searchHistory.isVisible =
                searchEditText.text.isEmpty() && trackHistoryAdapter.tracks.isNotEmpty()
            if (searchHistory.isVisible) {
                rvTracksHistory.removeAllViews()
            }
        }
    }

    private fun startWalkmanActivity(track: Track) {
        if (clickDebounce()) {
            findNavController()
                .navigate(
                    R.id.action_searchFragment_to_walkmanActivity,
                    WalkmanActivity.createArgs(track)
                )
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}