package com.practicum.playlist_maker.di

import com.practicum.playlist_maker.search.ui.SearchViewModel
import com.practicum.playlist_maker.settings.ui.SettingsViewModel
import com.practicum.playlist_maker.walkman.ui.WalkmanViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        WalkmanViewModel(get())
    }
}