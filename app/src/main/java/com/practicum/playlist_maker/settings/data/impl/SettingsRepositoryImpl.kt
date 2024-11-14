package com.practicum.playlist_maker.settings.data.impl

import com.practicum.playlist_maker.utils.App
import com.practicum.playlist_maker.settings.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val application: App) : SettingsRepository {
    override fun getThemeSettings(): Boolean {
        return application.darkTheme
    }

    override fun switchThemeSettings(switch: Boolean) {
        application.switchTheme(switch)
    }

    override fun checkSwitchOnResume(): Boolean {
        return application.checkSwitchOnResume()
    }
}