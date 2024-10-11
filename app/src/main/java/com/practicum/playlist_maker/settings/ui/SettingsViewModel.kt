package com.practicum.playlist_maker.settings.ui

import androidx.lifecycle.ViewModel
import com.practicum.playlist_maker.settings.domain.api.SettingsInteractor
import com.practicum.playlist_maker.sharing.domain.api.SharingInteractor


class SettingsViewModel(
        private val settingsInteractor: SettingsInteractor,
        private val sharingInteractor: SharingInteractor,
    ): ViewModel() {

    fun getTheme(): Boolean{
        return settingsInteractor.getThemeSettings()
    }

    fun switchTheme(switch: Boolean) {
        settingsInteractor.switchThemeSettings(switch)
    }

    fun checkSwitchOnResume(): Boolean{
        return settingsInteractor.checkSwitchOnResume()
    }

    fun share() {
        sharingInteractor.share()
    }

    fun support() {
        sharingInteractor.support()
    }

    fun userAgreement() {
        sharingInteractor.userAgreement()
    }
}