package com.practicum.playlist_maker.settings.domain.impl

import com.practicum.playlist_maker.settings.domain.api.SettingsInteractor
import com.practicum.playlist_maker.settings.domain.api.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun getThemeSettings(): Boolean {
        return repository.getThemeSettings()
    }

    override fun switchThemeSettings(switch: Boolean) {
        repository.switchThemeSettings(switch)
    }

    override fun checkSwitchOnResume(): Boolean {
        return repository.checkSwitchOnResume()
    }
}
