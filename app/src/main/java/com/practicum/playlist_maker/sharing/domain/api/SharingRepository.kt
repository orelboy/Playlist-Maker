package com.practicum.playlist_maker.sharing.domain.api

interface SharingRepository {
    fun share()
    fun support()
    fun userAgreement()
}