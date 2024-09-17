package com.practicum.playlist_maker.domain.impl

import com.practicum.playlist_maker.domain.api.SettingsInteractor
import com.practicum.playlist_maker.domain.api.SettingsRepository

class SettingsInteractorImpl (private val repository: SettingsRepository) : SettingsInteractor {
    override fun share() {
        repository.share()
    }

    override fun support() {
        repository.support()
    }

    override fun userAgreement() {
        repository.userAgreement()
    }
}