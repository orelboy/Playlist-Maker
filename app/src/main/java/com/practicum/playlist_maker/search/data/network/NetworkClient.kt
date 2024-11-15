package com.practicum.playlist_maker.search.data.network

import com.practicum.playlist_maker.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response

}