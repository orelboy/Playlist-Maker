package com.practicum.playlist_maker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.FragmentFavouriteListBinding
import com.practicum.playlist_maker.medialibrary.domain.models.FavoritesViewState
import com.practicum.playlist_maker.search.domain.models.Track
import com.practicum.playlist_maker.search.ui.SearchTracksAdapter
import com.practicum.playlist_maker.walkman.ui.WalkmanActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavouriteListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavoritesViewModel>()
    private var trackAdapter = SearchTracksAdapter()
    private var isClickAllowed = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavouriteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        trackAdapter = SearchTracksAdapter { track ->
            startWalkmanActivity(track)
        }

        binding.rvTracks.adapter = trackAdapter
    }

    private fun render(state: FavoritesViewState){
        when (state) {
            is FavoritesViewState.Empty -> showEmptyListError()
            is FavoritesViewState.Content -> showListTracks(state.favoritesTrackList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.readFavoritesTracks()
    }

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }

    private fun showEmptyListError(){
        binding.apply {
            textError.isVisible = true
            imageError.isVisible = true
            rvTracks.isVisible = false
        }
    }

    private fun showListTracks(trackList: List<Track>){
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(trackList)
        binding.apply {
            textError.isVisible = false
            imageError.isVisible = false
            rvTracks.isVisible = true
            rvTracks.removeAllViews()

        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1000L)
                isClickAllowed = true
            }
        }
        return current
    }
    private fun startWalkmanActivity(track: Track) {
        if (clickDebounce()) {
            findNavController()
                .navigate(
                    R.id.action_mediaLibraryFragment_to_walkmanActivity,
                    WalkmanActivity.createArgs(track)
                )
        }
    }

}