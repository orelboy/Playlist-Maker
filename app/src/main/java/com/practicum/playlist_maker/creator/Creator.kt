package com.practicum.playlist_maker.creator

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.practicum.playlist_maker.creator.App.Companion.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlist_maker.search.data.impl.HistoryRepositoryImpl
import com.practicum.playlist_maker.sharing.data.SharingRepositoryImpl
import com.practicum.playlist_maker.search.data.impl.TracksRepositoryImpl
import com.practicum.playlist_maker.walkman.data.WalkmanRepositoryImpl
import com.practicum.playlist_maker.search.data.network.RetrofitNetworkClient
import com.practicum.playlist_maker.search.domain.api.HistoryInteractor
import com.practicum.playlist_maker.search.domain.api.HistoryRepository
import com.practicum.playlist_maker.sharing.domain.api.SharingInteractor
import com.practicum.playlist_maker.sharing.domain.api.SharingRepository
import com.practicum.playlist_maker.search.domain.api.TracksInteractor
import com.practicum.playlist_maker.search.domain.api.TracksRepository
import com.practicum.playlist_maker.walkman.domain.api.WalkmanInteractor
import com.practicum.playlist_maker.walkman.domain.api.WalkmanRepository
import com.practicum.playlist_maker.search.domain.impl.HistoryInteractorImpl
import com.practicum.playlist_maker.sharing.domain.impl.SharingInteractorImpl
import com.practicum.playlist_maker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlist_maker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlist_maker.settings.domain.api.SettingsInteractor
import com.practicum.playlist_maker.settings.domain.api.SettingsRepository
import com.practicum.playlist_maker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlist_maker.walkman.domain.impl.WalkmanInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient()
        )
    }

    fun provideTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSearchHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(
                context.getSharedPreferences(
                    PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE
                )
        )
    }

    fun provideSearchHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    private lateinit var appContext: Context
    fun setContext(context: Context) {
        appContext = context.applicationContext
    }

    private fun getSettingsRepository(application: App): SettingsRepository {
        return SettingsRepositoryImpl(application)
    }

    fun provideSettingsInteractor(application: App): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(application))
    }

    private fun getSharingRepository(): SharingRepository {
        return SharingRepositoryImpl(appContext)
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getSharingRepository())
    }

    private fun getWalkmanRepository(): WalkmanRepository {
        return WalkmanRepositoryImpl()
    }

    fun provideWalkmanInteractor(): WalkmanInteractor {
        return WalkmanInteractorImpl(getWalkmanRepository())
    }
}