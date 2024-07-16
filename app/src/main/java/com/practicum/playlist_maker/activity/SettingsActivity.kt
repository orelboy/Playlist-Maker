package com.practicum.playlist_maker.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlist_maker.App
import com.practicum.playlist_maker.R


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

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val prefs = applicationContext as App
        themeSwitcher.isChecked = prefs.darkTheme
        themeSwitcher.setOnClickListener { view->
            val checked = (view as SwitchMaterial).isChecked
            prefs.switchTheme(checked)
            prefs.saveTheme(checked)
        }
    }

    override fun onResume(){
        super.onResume()
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val prefs = applicationContext as App
        if (AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM == AppCompatDelegate.getDefaultNightMode()){
            when (resources.configuration.uiMode and  Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    themeSwitcher.isChecked = false
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    themeSwitcher.isChecked = true
                }
            }
        }
        else{
            themeSwitcher.isChecked = prefs.darkTheme
        }
    }
}