package com.practicum.playlist_maker.sharing.domain.api

interface SharingRepository {
    fun share(link: String)
    fun support()
    fun userAgreement()
}