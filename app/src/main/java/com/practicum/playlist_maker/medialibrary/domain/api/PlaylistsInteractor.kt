package com.practicum.playlist_maker.medialibrary.domain.api

import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistEditData
import com.practicum.playlist_maker.medialibrary.domain.models.ResultAddingTrackInPlaylist
import com.practicum.playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun createPlaylist(data: PlaylistEditData): Flow<Playlist>
    suspend fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackInPlaylist(track: Track, playlist: Playlist) :Flow<ResultAddingTrackInPlaylist>

}