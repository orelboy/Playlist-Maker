package com.practicum.playlist_maker.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist_maker.creator.App
import com.practicum.playlist_maker.creator.Creator
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

    companion object {
        fun getViewModelFactory(application: App): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val settingsInteractor = Creator.provideSettingsInteractor(application)
                val sharingInteractor = Creator.provideSharingInteractor()

                SettingsViewModel(
                    settingsInteractor,
                    sharingInteractor
                )
            }
        }
    }
}