package com.practicum.playlist_maker.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchAPI {
    @GET("/search")
    fun search(
        @Query("entity") entity: String,
        @Query("term") text: String//,
      //  @Query("limit") limit: Int = 100,
      //  @Query("offset") offset: Int = 0
    ): Call<SearchTracksResponse>
}