package com.practicum.playlist_maker.presentation.settings

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlist_maker.App
import com.practicum.playlist_maker.Creator
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val interactor by lazy { Creator.provideSettingsInteractor() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        val imageClickListener: View.OnClickListener = View.OnClickListener { finish() }
        binding.back.setOnClickListener(imageClickListener)

        binding.sharing.setOnClickListener {
            interactor.share()
        }

        binding.support.setOnClickListener{
            interactor.support()
        }

        binding.userAgreement.setOnClickListener{
            interactor.userAgreement()
        }

        val prefs = applicationContext as App
        binding.themeSwitcher.isChecked = prefs.darkTheme
        binding.themeSwitcher.setOnClickListener { view->
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