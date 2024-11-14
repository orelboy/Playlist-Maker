package com.practicum.playlist_maker.search.data.network

import com.practicum.playlist_maker.search.data.dto.Response
import com.practicum.playlist_maker.search.data.dto.SearchTracksRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient ( private val itunesService: ITunesSearchAPI): NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return if (dto is SearchTracksRequest) {
            withContext(Dispatchers.IO){
                try {
                    val resp = itunesService.search( dto.expression)
                    resp.apply { resultCode = 200 }
                }catch (e: Throwable){
                    Response().apply { resultCode = 500 }
                }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}