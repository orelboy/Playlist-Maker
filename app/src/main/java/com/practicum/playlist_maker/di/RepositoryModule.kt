package com.practicum.playlist_maker.di

import android.content.Context
import com.practicum.playlist_maker.medialibrary.data.FavoritesRepositoryImpl
import com.practicum.playlist_maker.medialibrary.data.PlaylistsRepositoryImpl
import com.practicum.playlist_maker.medialibrary.domain.api.FavoritesRepository
import com.practicum.playlist_maker.medialibrary.domain.api.PlaylistsRepository
import com.practicum.playlist_maker.utils.App
import com.practicum.playlist_maker.search.data.impl.HistoryRepositoryImpl
import com.practicum.playlist_maker.search.data.impl.TracksRepositoryImpl
import com.practicum.playlist_maker.search.domain.api.HistoryRepository
import com.practicum.playlist_maker.search.domain.api.TracksRepository
import com.practicum.playlist_maker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlist_maker.settings.domain.api.SettingsRepository
import com.practicum.playlist_maker.sharing.data.SharingRepositoryImpl
import com.practicum.playlist_maker.sharing.domain.api.SharingRepository
import com.practicum.playlist_maker.walkman.data.WalkmanRepositoryImpl
import com.practicum.playlist_maker.walkman.domain.api.WalkmanRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(androidApplication() as App)
    }

    single<SharingRepository> {
        SharingRepositoryImpl(androidApplication() as Context)
    }

    single<WalkmanRepository> {
        WalkmanRepositoryImpl(get())
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    single<PlaylistsRepository>{
        PlaylistsRepositoryImpl(get(), get(), get(), get(), get())
    }

}