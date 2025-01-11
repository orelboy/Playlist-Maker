package com.practicum.playlist_maker.medialibrary.data.db

import android.net.Uri
import com.practicum.playlist_maker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlist_maker.medialibrary.domain.models.Playlist
import com.practicum.playlist_maker.medialibrary.domain.models.PlaylistCreateData

class PlaylistDbMapper {

    fun map(data: PlaylistCreateData, coverLocalPath: String?): PlaylistEntity {

        return PlaylistEntity(
            name = data.name,
            description = data.description,
            coverLocalPath = coverLocalPath,
            tracksIds = "",
            tracksCount = 0
        )

    }

    fun map(
        data: PlaylistEntity,
        coverPathUri: Uri?
    ): Playlist {

        return Playlist(
            id = data.id,
            name = data.name,
            description = data.description,
            coverLocalPath = data.coverLocalPath,
            tracksIds = data.tracksIds,
            tracksCount = data.tracksCount,

            coverPathUri = coverPathUri
        )
    }

}