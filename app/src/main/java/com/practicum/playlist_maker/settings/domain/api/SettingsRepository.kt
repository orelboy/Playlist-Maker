package com.practicum.playlist_maker.settings.domain.api

interface SettingsRepository {
    fun getThemeSettings(): Boolean
    fun switchThemeSettings(switch: Boolean)
    fun checkSwitchOnResume(): Boolean
}