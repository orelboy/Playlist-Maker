package com.practicum.playlist_maker.search.data.network

import com.practicum.playlist_maker.search.data.dto.Response
import com.practicum.playlist_maker.search.data.dto.SearchTracksRequest

class RetrofitNetworkClient ( private val itunesService: ITunesSearchAPI): NetworkClient {

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