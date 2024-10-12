package com.practicum.playlist_maker.di

import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import com.practicum.playlist_maker.creator.App.Companion.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlist_maker.search.data.network.ITunesSearchAPI
import com.practicum.playlist_maker.search.data.network.NetworkClient
import com.practicum.playlist_maker.search.data.network.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ITUNES_BASE_URL = "https://itunes.apple.com"

val dataModule = module {

    single<ITunesSearchAPI> {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchAPI::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<MediaPlayer> {
        MediaPlayer()
    }

    single{
        androidContext().getSharedPreferences(
            PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE
        )
    }
}