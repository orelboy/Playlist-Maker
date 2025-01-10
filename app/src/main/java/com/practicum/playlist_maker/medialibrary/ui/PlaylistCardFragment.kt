package com.practicum.playlist_maker.medialibrary.ui

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.FragmentPlaylistCardBinding
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistCardState
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistDetailedInfo
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistItemShareState
import com.practicum.playlist_maker.search.domain.models.Track
import com.practicum.playlist_maker.utils.AndroidUtils.dpToPx
import com.practicum.playlist_maker.utils.AndroidUtils.trackDurationString
import com.practicum.playlist_maker.utils.AndroidUtils.tracksCountString
import com.practicum.playlist_maker.utils.debounce
import com.practicum.playlist_maker.walkman.ui.WalkmanFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistCardFragment : Fragment() {

    private var _binding: FragmentPlaylistCardBinding? = null
    private val binding: FragmentPlaylistCardBinding get() = _binding!!

    private val viewModel by viewModel<PlaylistCardViewModel>()

    private var adapter = PlaylistTrackAdapter()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var menuSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlaylistCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = requireArguments().getInt(PLAYLIST_ID_KEY)

        adapter = PlaylistTrackAdapter(
            onClick = { track ->
                startWalkmanFragment(track)
            },
            onLongClick = { track ->
                confirmRemoveTrackDialog(track).show()
            }
        )

        binding.rvTracklist.adapter = adapter
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
        binding.playlistShare.setOnClickListener { viewModel.sharePlaylist() }

        binding.playlistMenu.setOnClickListener { viewModel.getCommandsList() }

        initBottomSheetBehavor()
        initMenuViews(playlistId)

        viewModel.updatePlaylistInfoById(playlistId)

        viewModel.stateLiveDataObserver().observe(viewLifecycleOwner) { render(it) }
        viewModel.shareStateLiveDataObserver().observe(viewLifecycleOwner) { renderShareState(it) }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBottomSheetBehavor() {

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomsheet)
            .apply {
                state = BottomSheetBehavior.STATE_COLLAPSED

                binding.playlistsBottomsheet.post {

                    val height = binding.frameLayoutIndent.height - 70

                    if (bottomSheetBehavior.peekHeight != height) {
                        bottomSheetBehavior.peekHeight = height
                    }
                }

            }


        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

    }

    private fun initMenuViews(playlistId: Int) {

        menuSheetBehavior = BottomSheetBehavior.from(binding.menu)
            .apply { state = BottomSheetBehavior.STATE_HIDDEN }
        menuSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                setPlaylistVisible(newState != BottomSheetBehavior.STATE_COLLAPSED)
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    viewModel.updatePlaylistInfoByLast()
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.btnMenuShare.setOnClickListener {
            viewModel.updatePlaylistInfoByLast()
            viewModel.sharePlaylist()
        }
        binding.btnMenuEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistCardFragment_to_playlistEditFragment,
                PlaylistEditFragment.createArgs(playlistId = playlistId)
            )
        }
        binding.btnMenuDelete.setOnClickListener {
            confirmDeleteDialog().show()
            viewModel.updatePlaylistInfoByLast()
        }

    }

    private fun render(state: PlaylistCardState) {

        when (state) {
            PlaylistCardState.Loading -> {}
            is PlaylistCardState.Content -> showPlaylistDetales(state.data)
            is PlaylistCardState.Commands -> showComandsPanel(state.data)
            PlaylistCardState.Deleted -> {
                findNavController().popBackStack()
            }
        }

    }

    private fun renderShareState(state: PlaylistItemShareState) {
        when (state) {
            PlaylistItemShareState.Empty -> {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.no_tracklist_in_playlist_shared),
                    Toast.LENGTH_SHORT
                )
                    .show()

            }

            PlaylistItemShareState.None -> {}
        }

    }

    private fun showPlaylistDetales(data: PlaylistDetailedInfo) {

        showPlaylistCover(data.coverPathUri?.toUri())

        updateDescriptioView(data)

        setCommandMenuVisible(isVisible = false)

        updateTracklist(data.tracks)

    }

    private fun updateDescriptioView(data: PlaylistDetailedInfo) {
        binding.playlistName.text = data.name
        binding.playlistDescription.isVisible = !data.description.isNullOrEmpty()
        binding.playlistDescription.text = data.description
        binding.playlistDuration.text =
            trackDurationString(requireContext(), data.totalDuration)
        binding.tracksCount.text =
            tracksCountString(requireContext(), data.tracksCount)
    }

    private fun showComandsPanel(data: PlaylistDetailedInfo) {

        setCommandMenuVisible(isVisible = true)
        showPlaylistCover(data.coverPathUri?.toUri())
        updateDescriptioView(data)

        binding.itemFromMenu.playlistName.text = data.name
        binding.itemFromMenu.tracksCount.text = tracksCountString(
            requireContext(), data.tracksCount
        )

        Glide.with(requireContext())
            .load(data.coverPathUri)
            .placeholder(R.drawable.placeholder_ic_album)
            .centerCrop()
            .transform(RoundedCorners(2.dpToPx(requireContext())))
            .into(binding.itemFromMenu.cover)

        setPlaylistVisible(isVisible = false)

    }

    private fun setCommandMenuVisible(isVisible: Boolean) {
        binding.overlay.isVisible = isVisible

        menuSheetBehavior.state =
            if (isVisible) BottomSheetBehavior.STATE_COLLAPSED
            else BottomSheetBehavior.STATE_HIDDEN

    }

    private fun showPlaylistCover(coverUri: Uri?) {

        binding.imageAlbum.let {
            Glide.with(this)
                .load(coverUri)
                .placeholder(R.drawable.ic_placeholder_312)
                .fitCenter()
                .transform(CenterCrop())
                .into(it)
        }

    }

    private fun setPlaylistVisible(isVisible: Boolean) {
        if (binding.playlistsBottomsheet.isVisible != isVisible) {
            binding.playlistsBottomsheet.isVisible = isVisible
        }
    }

    private fun updateTracklist(tracks: List<Track>) {

        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()

        binding.rvTracklist.isVisible = tracks.isNotEmpty()
        if (tracks.isEmpty()){
                val text = getString(R.string.empty_playlist)
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmRemoveTrackDialog(track: Track): MaterialAlertDialogBuilder {

        val title = getString(R.string.delete_track)
        val message = getString(R.string.question_sure_remove_track_from_playlist)
        val no = getString(R.string.no)
        val yes = getString(R.string.yes)

        return MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton(no) { _, _ -> }
            .setPositiveButton(yes) { _, _ -> viewModel.removeFromPlaylist(track) }
    }

    private fun confirmDeleteDialog(): MaterialAlertDialogBuilder {
        val title = getString(R.string.delete_playlist)
        val message = getString(R.string.question_sure_delete_playlist).replace("%1", binding.playlistName.text.toString())
        val no = getString(R.string.no)
        val yes = getString(R.string.yes)

        return MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton(no) { _, _ -> }
            .setPositiveButton(yes) { _, _ -> viewModel.deletePlaylist() }
    }

    private val playTrackDebouncer: (Track) -> Unit by lazy {
        debounce(CLICK_DEBOUNCE_DELAY, lifecycleScope, false) { track ->
            findNavController()
                .navigate(
                    R.id.action_playlistCardFragment_to_walkmanFragment,
                    WalkmanFragment.createArgs(track)
                )
        }
    }

    private fun startWalkmanFragment(track: Track) {
        playTrackDebouncer(track)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
        private const val PLAYLIST_ID_KEY = "playlistId"
        fun createArgs(playlist: Playlist) = bundleOf(PLAYLIST_ID_KEY to playlist.id)

    }

}