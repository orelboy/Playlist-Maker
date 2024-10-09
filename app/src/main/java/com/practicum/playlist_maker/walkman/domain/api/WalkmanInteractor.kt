package com.practicum.playlist_maker.walkman.domain.api

import com.practicum.playlist_maker.walkman.domain.models.PlayerState

interface WalkmanInteractor {
    fun preparePlayer(url: String)
    fun setOnPreparedListener(listener: (PlayerState) -> Unit)
    fun setOnCompletionListener(listener: (PlayerState) -> Unit)

    fun startPlayer()
    fun pausePlayer()

    fun getCurrentPosition(): Int
    fun getRelease()
}