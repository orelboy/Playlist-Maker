package com.practicum.playlist_maker.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {
    override fun share() {
        val shareIntent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, context.getString(R.string.url_YP_android_developer))
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun support() {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_email)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.subject_support_mail))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.text_support_mail))
        }
        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(supportIntent)
    }

    override fun userAgreement() {
        val userAgreementIntent = Intent(Intent.ACTION_VIEW,
            Uri.parse (context.getString(R.string.url_offer)))
        userAgreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(userAgreementIntent)
    }

}