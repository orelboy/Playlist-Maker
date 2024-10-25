package com.practicum.playlist_maker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlist_maker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
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