package com.practicum.playlist_maker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlist_maker.creator.App
import com.practicum.playlist_maker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(application as App)
        )[SettingsViewModel::class.java]

        binding.apply {
            back.setOnClickListener {
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