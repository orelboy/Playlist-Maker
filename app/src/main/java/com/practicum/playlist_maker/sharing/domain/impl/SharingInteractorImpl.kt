package com.practicum.playlist_maker.sharing.domain.impl

import com.practicum.playlist_maker.sharing.domain.api.SharingInteractor
import com.practicum.playlist_maker.sharing.domain.api.SharingRepository

class SharingInteractorImpl (private val repository: SharingRepository) : SharingInteractor {
    override fun share(link: String) {
        repository.share(link)
    }

    override fun support() {
        repository.support()
    }

    override fun userAgreement() {
        repository.userAgreement()
    }
}