package com.practicum.playlist_maker.di

import com.practicum.playlist_maker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlist_maker.medialibrary.domain.impl.FavoritesInteractorImpl
import com.practicum.playlist_maker.search.domain.api.HistoryInteractor
import com.practicum.playlist_maker.search.domain.api.TracksInteractor
import com.practicum.playlist_maker.search.domain.impl.HistoryInteractorImpl
import com.practicum.playlist_maker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlist_maker.settings.domain.api.SettingsInteractor
import com.practicum.playlist_maker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlist_maker.sharing.domain.api.SharingInteractor
import com.practicum.playlist_maker.sharing.domain.impl.SharingInteractorImpl
import com.practicum.playlist_maker.walkman.domain.api.WalkmanInteractor
import com.practicum.playlist_maker.walkman.domain.impl.WalkmanInteractorImpl
import org.koin.dsl.module
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val interactorModule = module {

    factory<ExecutorService> {
        Executors.newCachedThreadPool()
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<WalkmanInteractor> {
        WalkmanInteractorImpl(get())
    }

    factory<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
}