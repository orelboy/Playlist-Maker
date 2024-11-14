package com.practicum.playlist_maker.search.data.network

import com.practicum.playlist_maker.search.data.dto.SearchTracksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchAPI {
    @GET("/search?entity=song")
    suspend fun search(
        @Query("term") text: String
    ): SearchTracksResponse
}