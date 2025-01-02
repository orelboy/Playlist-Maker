package com.practicum.playlist_maker.walkman.domain.impl

import com.practicum.playlist_maker.walkman.domain.api.WalkmanInteractor
import com.practicum.playlist_maker.walkman.domain.api.WalkmanRepository
import com.practicum.playlist_maker.walkman.domain.models.WalkmanState

class WalkmanInteractorImpl( private val repository: WalkmanRepository) : WalkmanInteractor {

    override fun preparePlayer(url: String) {
        repository.preparePlayer(url)
    }
    override fun setOnPreparedListener(listener: (WalkmanState) -> Unit) {
        repository.setOnPreparedListener(listener)
    }

    override fun setOnCompletionListener(listener: (WalkmanState) -> Unit) {
        repository.setOnCompletionListener(listener)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun getCurrentPosition(): Int {
        return  repository.getCurrentPosition()
    }

    override fun getRelease() {
        repository.getRelease()
    }

}