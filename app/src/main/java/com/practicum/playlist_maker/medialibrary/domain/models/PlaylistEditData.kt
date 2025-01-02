package com.practicum.playlist_maker.medialibrary.domain.models

import android.net.Uri

data class PlaylistEditData(
    val name: String,
    val description: String?,
    val coverPathUri: Uri?
)