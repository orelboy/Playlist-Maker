package com.practicum.playlist_maker.medialibrary.domain.impl

import com.practicum.playlist_maker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlist_maker.medialibrary.domain.api.PlaylistsRepository
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistEditData
import com.practicum.playlist_maker.medialibrary.domain.models.ResultAddingTrackInPlaylist
import com.practicum.playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository): PlaylistsInteractor {
    override suspend fun createPlaylist(data: PlaylistEditData): Flow<Playlist> {
        return repository.createPlaylist(data)
    }
    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }
    override suspend fun addTrackInPlaylist(track: Track, playlist: Playlist) :Flow<ResultAddingTrackInPlaylist>{
        return repository.addTrack(track, playlist)
    }

}