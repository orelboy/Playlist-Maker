package com.practicum.playlist_maker.domain.impl

import com.practicum.playlist_maker.domain.api.WalkmanInteractor
import com.practicum.playlist_maker.domain.api.WalkmanRepository
import com.practicum.playlist_maker.domain.models.PlayerState

class WalkmanInteractorImpl( private val repository: WalkmanRepository ) : WalkmanInteractor {

    override fun preparePlayer(url: String) {
        repository.preparePlayer(url)
    }
    override fun setOnPreparedListener(listener: (PlayerState) -> Unit) {
        repository.setOnPreparedListener(listener)
    }

    override fun setOnCompletionListener(listener: (PlayerState) -> Unit) {
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