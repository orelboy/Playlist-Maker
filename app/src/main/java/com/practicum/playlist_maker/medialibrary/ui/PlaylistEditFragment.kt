package com.practicum.playlist_maker.medialibrary.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditFragment : PlayListCreateFragment() {

    override val viewModel by viewModel<PlaylistEditViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createButton.text = getString(R.string.save)
        binding.toolbar.title = getString(R.string.edit)

        binding.toolbar.setOnClickListener {
            findNavController().popBackStack()
        }
        onBackCallBack.isEnabled = false
        viewModel.fillByPlaylistId(requireArguments().getInt(PLAYLIST_KEY))

    }

    override fun showCreate(newPlaylist: Playlist) {
        closeFragment()
    }

    companion object {
        private const val PLAYLIST_KEY = "playlistKey"
        fun createArgs(playlistId: Int) = bundleOf(PLAYLIST_KEY to playlistId)
    }

}