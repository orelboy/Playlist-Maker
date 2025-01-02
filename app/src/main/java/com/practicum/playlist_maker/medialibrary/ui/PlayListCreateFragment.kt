package com.practicum.playlist_maker.medialibrary.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.FragmentPlayListEditBinding
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistEditData
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistEditState
import com.practicum.playlist_maker.utils.AndroidUtils.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListCreateFragment : Fragment() {
    private val viewModel: PlayListCreateViewModel by viewModel()

    private var _binding: FragmentPlayListEditBinding? = null
    private val binding: FragmentPlayListEditBinding get() = _binding!!

    private lateinit var nameTextWatcher: TextWatcher
    private lateinit var descriptionTextWatcher: TextWatcher

    private val onBackCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            safeExit(isEnabled)
        }
    }

    private val dialog by lazy {
        val title = getString(R.string.finish_creating_the_playlist)
        val message = getString(R.string.all_unsaved_data_will_be_lost)
        val cancel = getString(R.string.cancel)
        val finish = getString(R.string.finish)

        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton(cancel) { _, _ -> }
            .setPositiveButton(finish) { _, _ -> closeFragment() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlayListEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnClickListener {
            safeExit()
        }

        initTextWatchers()

        viewModel.playListStateObserver().observe(viewLifecycleOwner) { render(it) }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                viewModel.onCoverChanged(uri)
            }
        binding.playlistCover.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        binding.createButton.setOnClickListener {
            viewModel.savePlaylist()
        }

        initOnBackPressedDispatcher()
    }

    private fun initOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackCallBack)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyTextWatchers()
        _binding = null
    }

    private fun initTextWatchers() {

        nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onNameChanged(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        nameTextWatcher.let {
            binding.playlistName.addTextChangedListener(it)
        }

        descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onDescriptionChanged(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        descriptionTextWatcher.let {
            binding.playlistDescription.addTextChangedListener(it)
        }

    }

    private fun destroyTextWatchers() {
        nameTextWatcher.let {
            binding.playlistName.removeTextChangedListener(it)
        }
        descriptionTextWatcher.let {
            binding.playlistDescription.removeTextChangedListener(it)
        }
    }

    private fun safeExit(isEnabled: Boolean = true) {

        if (isEnabled && viewModel.hasUnsavedModify()) {
            dialog.show()
        } else {
            closeFragment()
        }

    }

    private fun closeFragment() {
        findNavController().popBackStack()
    }

    private fun render(state: PlaylistEditState) {
        when (state) {
            is PlaylistEditState.Content -> showPlaylistContent(state.data)
            PlaylistEditState.Empty -> showEmpty()
            PlaylistEditState.Loading -> {}
            is PlaylistEditState.Create -> showCreate(state.data)
        }
    }

    private fun showPlaylistContent(data: PlaylistEditData) {

        if (binding.playlistName.text.toString() != data.name) {
            binding.playlistName.setText(data.name)
        }
        if (binding.playlistName.text.isNotEmpty()) {
            binding.playlistNameTitle.isVisible = true
            binding.playlistName.isSelected = true
            binding.createButton.isEnabled = true
        } else {
            binding.playlistNameTitle.isVisible = false
            binding.playlistName.isSelected = false
            binding.createButton.isEnabled = false
        }

        if (binding.playlistDescription.text.toString() != data.description) {
            binding.playlistDescription.setText(data.description)
        }
        if (binding.playlistDescription.text.isNotEmpty()) {
            binding.playlistDescriptionTitle.isVisible = true
            binding.playlistDescription.isSelected = true
        } else {
            binding.playlistDescriptionTitle.isVisible = false
            binding.playlistDescription.isSelected = false
        }

        showPlaylistCoverByUri(data.coverPathUri)

    }

    private fun showPlaylistCoverByUri(uri: Uri?) {

        if (uri != null) {

            val imageView = binding.playlistCover
            Glide.with(requireContext())
                .load(uri)
                .centerCrop()
                .transform(CenterCrop(), RoundedCorners(8.dpToPx(imageView.context.applicationContext)))
                .into(imageView)
        }

    }

    private fun showEmpty() {
        showPlaylistContent(
            PlaylistEditData(
                name = "",
                description = "",
                coverPathUri = null
            )
        )
    }

    private fun showCreate(newPlaylist: Playlist) {

        Toast.makeText(
            requireActivity(),
            "Плейлист ${newPlaylist.name} создан",
            Toast.LENGTH_SHORT
        ).show()

        closeFragment()

    }

}