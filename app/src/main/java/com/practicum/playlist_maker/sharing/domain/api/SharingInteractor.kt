package com.practicum.playlist_maker.sharing.domain.api

interface SharingInteractor {
    fun share(link: String)
    fun support()
    fun userAgreement()
}