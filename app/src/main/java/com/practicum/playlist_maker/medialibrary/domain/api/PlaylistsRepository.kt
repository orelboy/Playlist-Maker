package com.practicum.playlist_maker.medialibrary.domain.api

import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistCreateData
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistEditData
import com.practicum.playlist_maker.medialibrary.domain.models.ResultAddingTrackInPlaylist
import com.practicum.playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun createPlaylist(data: PlaylistCreateData): Flow<Playlist>
    fun getAllPlaylists(): Flow<List<Playlist>>
    fun addTrack(track: Track, playlist: Playlist): Flow<ResultAddingTrackInPlaylist>

    fun deletePlaylist(playlistId: Int): Flow<Boolean>

    fun savePlaylistData(data: PlaylistEditData): Flow<Playlist>
    fun getPlaylistById(playlistId: Int): Flow<Playlist?>

    fun removeTrack(track: Track, playlistId: Int): Flow<Boolean>
    fun getTracks(playlistId: Int): Flow<List<Track>>

}