package com.practicum.playlist_maker.search.data.network

import com.practicum.playlist_maker.search.data.dto.Response
import com.practicum.playlist_maker.search.data.dto.SearchTracksRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesSearchAPI::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is SearchTracksRequest) {
            val resp = itunesService.search( dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}