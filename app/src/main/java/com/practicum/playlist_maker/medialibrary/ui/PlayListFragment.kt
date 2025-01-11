package com.practicum.playlist_maker.medialibrary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.FragmentPlayListBinding
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistViewState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {
    private val viewModel: PlayListViewModel by viewModel()

    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!

    private var playlistAdapter = PlayListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_playlistCreateFragment)
        }
        playlistAdapter = PlayListAdapter{ playlist ->
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_playlistCardFragment,
                PlaylistCardFragment.createArgs(playlist = playlist)
            )
        }
        binding.playlists.adapter = playlistAdapter

        viewModel.playlistStateObserver().observe(viewLifecycleOwner) { state -> render(state) }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlaylistViewState) {
        when (state) {
            is PlaylistViewState.Content -> renderContent(state.data)
            PlaylistViewState.Empty -> renderEmpty()
            PlaylistViewState.Loading -> renderLoading()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderContent(data: List<Playlist>) {
        playlistAdapter.playlists.clear()
        playlistAdapter.playlists.addAll(data)
        playlistAdapter.notifyDataSetChanged()

        binding.playlists.isVisible = true
        binding.imageError.isVisible = false
        binding.textError.isVisible = false
    }

    private fun renderLoading() {}

    private fun renderEmpty() {
        binding.playlists.isVisible = false
        binding.imageError.isVisible = true
        binding.textError.isVisible = true
    }

    companion object {
        fun newInstance(): PlayListFragment {
            return PlayListFragment()
        }
    }
}