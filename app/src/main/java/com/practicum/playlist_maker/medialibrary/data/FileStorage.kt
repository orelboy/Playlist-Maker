package com.practicum.playlist_maker.medialibrary.data

import android.net.Uri

interface FileStorage {
    suspend fun saveImage(name: String, uri: Uri?): String
    suspend fun getImageUri(name: String?): Uri?
    suspend fun deleteImage(name: String?):Boolean
}