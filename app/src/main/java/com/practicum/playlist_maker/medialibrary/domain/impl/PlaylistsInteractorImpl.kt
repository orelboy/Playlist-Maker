package com.practicum.playlist_maker.medialibrary.domain.impl

import com.practicum.playlist_maker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlist_maker.medialibrary.domain.api.PlaylistsRepository
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistCreateData
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistEditData
import com.practicum.playlist_maker.medialibrary.domain.models.ResultAddingTrackInPlaylist
import com.practicum.playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository): PlaylistsInteractor {
    override suspend fun createPlaylist(data: PlaylistCreateData): Flow<Playlist> {
        return repository.createPlaylist(data)
    }
    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }
    override suspend fun addTrackInPlaylist(track: Track, playlist: Playlist) :Flow<ResultAddingTrackInPlaylist>{
        return repository.addTrack(track, playlist)
    }
    override suspend fun saveEditPlaylistInfo(data: PlaylistEditData): Flow<Playlist> {
        return repository.savePlaylistData(data = data)
    }

    override suspend fun getPlaylistById(id: Int): Flow<Playlist?> {
        return repository.getPlaylistById(playlistId = id)
    }

    override suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>> {
        return repository.getTracks(playlistId = playlistId)
    }

    override suspend fun removeTrack(track: Track, playlistId: Int): Flow<Boolean> {
        return repository.removeTrack(track = track, playlistId = playlistId)
    }

    override suspend fun deletePlaylist(playlistId: Int): Flow<Boolean> {
        return repository.deletePlaylist(playlistId = playlistId)
    }
}