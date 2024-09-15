package com.practicum.playlist_maker

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.practicum.playlist_maker.App.Companion.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlist_maker.data.SearchHistoryRepositoryImpl
import com.practicum.playlist_maker.data.SettingsRepositoryImpl
import com.practicum.playlist_maker.data.TracksRepositoryImpl
import com.practicum.playlist_maker.data.WalkmanRepositoryImpl
import com.practicum.playlist_maker.data.network.RetrofitNetworkClient
import com.practicum.playlist_maker.domain.api.SearchHistoryInteractor
import com.practicum.playlist_maker.domain.api.SearchHistoryRepository
import com.practicum.playlist_maker.domain.api.SettingsInteractor
import com.practicum.playlist_maker.domain.api.SettingsRepository
import com.practicum.playlist_maker.domain.api.TracksInteractor
import com.practicum.playlist_maker.domain.api.TracksRepository
import com.practicum.playlist_maker.domain.api.WalkmanInteractor
import com.practicum.playlist_maker.domain.api.WalkmanRepository
import com.practicum.playlist_maker.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlist_maker.domain.impl.SettingsInteractorImpl
import com.practicum.playlist_maker.domain.impl.TracksInteractorImpl
import com.practicum.playlist_maker.domain.impl.WalkmanInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient()
        )
    }

    fun provideTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
                context.getSharedPreferences(
                    PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE
                )
        )
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getWalkmanRepository(): WalkmanRepository{
        return WalkmanRepositoryImpl()
    }

    fun provideWalkmanInteractor(): WalkmanInteractor {
        return WalkmanInteractorImpl(getWalkmanRepository())
    }
}