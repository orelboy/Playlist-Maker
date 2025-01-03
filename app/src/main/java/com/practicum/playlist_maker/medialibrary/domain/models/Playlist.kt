package com.practicum.playlist_maker.medialibrary.domain.models

import android.net.Uri

data class Playlist(
    val id: Int,
    val name: String,
    val description: String?,
    val coverLocalPath: String?,
    val tracksIds: String?,
    val tracksCount: Int?,
    val coverPathUri: Uri?
)