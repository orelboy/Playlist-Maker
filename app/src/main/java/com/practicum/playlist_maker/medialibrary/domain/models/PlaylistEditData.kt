package com.practicum.playlist_maker.medialibrary.domain.models

import android.net.Uri

class PlaylistEditData(
    val id: Int,
    name: String,
    description: String?,
    coverPathUri: Uri?,
) : PlaylistCreateData(name = name, description = description, coverPathUri = coverPathUri) {
}