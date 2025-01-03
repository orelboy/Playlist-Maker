package com.practicum.playlist_maker.walkman.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.search.domain.models.Track
import com.practicum.playlist_maker.utils.AndroidUtils.dpToPx
import com.practicum.playlist_maker.databinding.FragmentWalkmanBinding
import com.practicum.playlist_maker.medialibrary.domain.models.FavoritesState
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistTrackAddState
import com.practicum.playlist_maker.walkman.domain.models.WalkmanPlaylistViewState
import com.practicum.playlist_maker.walkman.domain.models.WalkmanState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class WalkmanFragment :  Fragment() {
    private var _binding: FragmentWalkmanBinding? = null
    private val binding get() = _binding!!

    private var track: Track? = null
    private var url: String? = null
    private val viewModel by viewModel<WalkmanViewModel>{parametersOf(track)}

    private var walkmanPlayListsAdapter = WalkmanPlayListsAdapter()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWalkmanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheet()

//        this.track = requireArguments().getSerializable(TRACK_KEY, Track::class.java)
        //оставил этот вариант так как на mi 9T крашилось
        this.track = requireArguments().getSerializable(TRACK_KEY) as Track

        viewModel.observePlayStatusState().observe(viewLifecycleOwner) {state ->
            when (state) {
                WalkmanState.STATE_DEFAULT -> {viewModel.preparePlayer(url)}
                WalkmanState.STATE_PREPARED -> preparePlayer()
                WalkmanState.STATE_PLAYING -> startPlayer()
                WalkmanState.STATE_PAUSED -> pausePlayer()
            }
        }
        viewModel.observeCurrentPositionState().observe(viewLifecycleOwner){
            binding.playTime.text = it
        }

        viewModel.observePlaylistState().observe(viewLifecycleOwner) { state ->

            if (bottomSheetBehavior.state != state.listState) {
                bottomSheetBehavior.state = state.listState
                updateOverlayVisible(state.listState)
            }

            when (state) {
                is WalkmanPlaylistViewState.Content -> updatePlaylistsData(state.data)
                is WalkmanPlaylistViewState.Loading -> {}
            }

        }

        viewModel.observeIsFavorite().observe(viewLifecycleOwner){ state ->
            when (state) {
                is FavoritesState.IsFavorites -> {
                    binding.addFavourite.setImageResource(
                        if (state.isFavorite)
                            R.drawable.ic_button_add_favorites_active
                        else
                            R.drawable.ic_button_add_favorites_inactive
                    )
                }
            }
        }

        viewModel.playlistTrackAddedStateObserver().observe(viewLifecycleOwner) { state ->

            when (state) {
                PlaylistTrackAddState.Empty -> {}
                PlaylistTrackAddState.Loading -> {}
                PlaylistTrackAddState.Error -> Toast.makeText(
                    requireContext(),
                    getString(R.string.something_went_wrong),
                    Toast.LENGTH_SHORT
                ).show()

                is PlaylistTrackAddState.TrackAdded -> {

                    viewModel.clearPlaylists(BottomSheetBehavior.STATE_HIDDEN)

                    showAddMessage(
                        state.playlist,
                        false
                    )
                }

                is PlaylistTrackAddState.TrackAddedEarly,
                -> showAddMessage(
                    state.playlist,
                    true
                )
            }

            if (state != PlaylistTrackAddState.Loading
                && state != PlaylistTrackAddState.Empty
            ) {
                viewModel.clearPlaylistTrackAddedMessage()
            }

        }

        fun getAlbumData(): String {
            return track?.collectionName ?: run {
                binding.album.isVisible = false
                binding.albumData.isVisible = false
                return ""
            }
        }

        binding.apply {
            backButton.setOnClickListener { findNavController().popBackStack() }
            playAndPause.setOnClickListener { viewModel.playbackControl() }
            addFavourite.setOnClickListener { viewModel.onFavoriteClicked() }
            trackName.text = track?.trackName
            artistName.text = track?.artistName
            durationData.text = track?.getTrackTime(track?.trackTime!!)
            albumData.text = getAlbumData()
            yearData.text = track?.releaseDate?.take(STR_YEAR_LENGTH)
            genreData.text = track?.primaryGenreName
            countryData.text = track?.country
            url = track?.previewUrl
        }

        binding.imageAlbum.let {
            Glide.with(this)
                .load(track?.getCoverArtwork())
                .placeholder(R.drawable.ic_placeholder_312)
                .fitCenter()
                .transform(RoundedCorners(ROUNDING_RADIUS.dpToPx(requireContext())))
                .into(it)
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetOnStateChanged(bottomSheetBehavior.state)
        }
    }

    private fun preparePlayer() {
        binding.playAndPause.isEnabled = true
        binding.playAndPause.setImageResource(R.drawable.ic_button_play)
    }

    private fun startPlayer() {
        binding.playAndPause.setImageResource(R.drawable.ic_button_pause)
    }

    private fun pausePlayer() {
        binding.playAndPause.setImageResource(R.drawable.ic_button_play)
    }

    private fun bottomSheet() {

        binding.overlay.isVisible = false
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomsheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                bottomSheetOnStateChanged(newState)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })

        binding.addPlaylist.setOnClickListener {
            viewModel.getAllPlaylists(BottomSheetBehavior.STATE_COLLAPSED)
        }

        walkmanPlayListsAdapter = WalkmanPlayListsAdapter { playlist ->
            viewModel.addTrackToPlaylist(playlist)
        }

        binding.playlistsRecyclerView.adapter = walkmanPlayListsAdapter
        binding.newPlaylistButton.setOnClickListener {
            viewModel.clearPlaylists(BottomSheetBehavior.STATE_HIDDEN)
            findNavController().navigate(R.id.action_walkmanFragment_to_playlistCreateFragment)
        }
    }

    private fun bottomSheetOnStateChanged(newState: Int) {
        updateOverlayVisible(newState)
        when (newState) {
            BottomSheetBehavior.STATE_COLLAPSED,
            BottomSheetBehavior.STATE_EXPANDED,
            -> viewModel.getAllPlaylists(newState)

            BottomSheetBehavior.STATE_HIDDEN -> viewModel.clearPlaylists(newState)
            else -> {}
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updatePlaylistsData(playlists: List<Playlist>) {
        walkmanPlayListsAdapter.playlists.clear()
        walkmanPlayListsAdapter.playlists.addAll(playlists)
        walkmanPlayListsAdapter.notifyDataSetChanged()
    }

    private fun updateOverlayVisible(listState: Int) {
        binding.overlay.isVisible = listState != BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showAddMessage(playlist: Playlist, addedEarly: Boolean) {

        val text =
            if (addedEarly) "Трек уже добавлен в плейлист ${playlist.name}" else "Добавлено в плейлист ${playlist.name}"

        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()

    }

    companion object {
        private const val STR_YEAR_LENGTH = 4
        private const val ROUNDING_RADIUS = 8
        private const val TRACK_KEY = "TRACK_KEY"

        fun createArgs(track: Track): Bundle {
            val args = Bundle()
            args.putSerializable (TRACK_KEY, track)
            return args
        }
    }
}
