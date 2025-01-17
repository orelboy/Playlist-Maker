package com.practicum.playlist_maker.di

import com.practicum.playlist_maker.medialibrary.ui.FavoritesViewModel
import com.practicum.playlist_maker.medialibrary.ui.PlayListCreateViewModel
import com.practicum.playlist_maker.medialibrary.ui.PlayListViewModel
import com.practicum.playlist_maker.medialibrary.ui.PlaylistCardViewModel
import com.practicum.playlist_maker.medialibrary.ui.PlaylistEditViewModel
import com.practicum.playlist_maker.search.domain.models.Track
import com.practicum.playlist_maker.search.ui.SearchViewModel
import com.practicum.playlist_maker.settings.ui.SettingsViewModel
import com.practicum.playlist_maker.walkman.ui.WalkmanViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {(track: Track?) ->
        WalkmanViewModel(get(),get(), get(), track)
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlayListViewModel(get())
    }

    viewModel {
        PlayListCreateViewModel(get())
    }

    viewModel {
        PlaylistEditViewModel(get())
    }

    viewModel {
        PlaylistCardViewModel(androidApplication(), get(), get())
    }
}