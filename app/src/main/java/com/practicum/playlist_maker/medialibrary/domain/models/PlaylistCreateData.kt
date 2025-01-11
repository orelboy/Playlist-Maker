package com.practicum.playlist_maker.medialibrary.domain.models

import android.net.Uri

open class PlaylistCreateData(
    val name: String,
    val description: String?,
    val coverPathUri: Uri?
)