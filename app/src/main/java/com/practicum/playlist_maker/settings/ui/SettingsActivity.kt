package com.practicum.playlist_maker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlist_maker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.apply {
            toolbar.setNavigationOnClickListener {
                finish()
            }
            sharing.setOnClickListener {
                viewModel.share()
            }
            support.setOnClickListener {
                viewModel.support()
            }
            userAgreement.setOnClickListener {
                viewModel.userAgreement()
            }
            themeSwitcher.isChecked = viewModel.getTheme()
            themeSwitcher.setOnClickListener { view ->
                val checked = (view as SwitchMaterial).isChecked
                viewModel.switchTheme(checked)
            }
        }
    }

    override fun onResume(){
        super.onResume()
        binding.themeSwitcher.isChecked = viewModel.checkSwitchOnResume()
    }
}