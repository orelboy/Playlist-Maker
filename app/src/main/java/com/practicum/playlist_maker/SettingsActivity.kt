package com.practicum.playlist_maker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView


class SettingsActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<ImageView>(R.id.back)
        val imageClickListener: View.OnClickListener = View.OnClickListener { finish() }
        buttonBack.setOnClickListener(imageClickListener)

        val sharing = findViewById<TextView>(R.id.sharing)
        sharing.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.url_YP_android_developer))
            startActivity(shareIntent)
        }

        val support = findViewById<TextView>(R.id.support)
        support.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_support_mail))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.text_support_mail))
            }
            startActivity(supportIntent)
        }

        val userAgreement = findViewById<TextView>(R.id.user_agreement)
        userAgreement.setOnClickListener{
            val userAgreementIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse (getString(R.string.url_offer)))
            startActivity(userAgreementIntent)
        }
    }
}