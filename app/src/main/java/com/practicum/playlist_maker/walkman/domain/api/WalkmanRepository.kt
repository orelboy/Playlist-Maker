package com.practicum.playlist_maker.walkman.domain.api

import com.practicum.playlist_maker.walkman.domain.models.WalkmanState

interface WalkmanRepository {
    fun preparePlayer(url: String)
    fun setOnPreparedListener(listener: (WalkmanState) -> Unit)
    fun setOnCompletionListener(listener: (WalkmanState) -> Unit)

    fun startPlayer()
    fun pausePlayer()

    fun getCurrentPosition(): Int
    fun getRelease()
}