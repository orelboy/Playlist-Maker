package com.practicum.playlist_maker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App  : Application() {
    var darkTheme = false
    private var sharedPrefs: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        if (sharedPrefs?.all.isNullOrEmpty() || sharedPrefs?.all?.keys!!.contains(THEME_SWITCHER_KEY).not()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            when (resources.configuration.uiMode and  Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    darkTheme = false
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    darkTheme = true
                }
            }
        }else{
            darkTheme = sharedPrefs?.getBoolean(THEME_SWITCHER_KEY, false)?:false
            switchTheme(darkTheme)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun saveTheme(darkTheme: Boolean) {
        sharedPrefs
            ?.edit()
            ?.putBoolean(THEME_SWITCHER_KEY, darkTheme)
            ?.apply()
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val THEME_SWITCHER_KEY = "key_for_theme_switcher"
    }
}