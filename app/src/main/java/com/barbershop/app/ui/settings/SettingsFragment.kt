package com.barbershop.app.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.barbershop.app.R
import com.barbershop.app.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    companion object {
        private const val PREFS_NAME = "theme_prefs"
        private const val KEY_IS_DARK_MODE = "is_dark_mode"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        setupDarkModeToggle()
        setupClickListeners()
    }

    private fun setupDarkModeToggle() {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean(KEY_IS_DARK_MODE, false)
        
        binding.switchDarkMode.isChecked = isDarkMode
        updateThemeStatusText(isDarkMode)
        updateThemeIcon(isDarkMode)

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(KEY_IS_DARK_MODE, isChecked).apply()
            updateThemeStatusText(isChecked)
            updateThemeIcon(isChecked)
            
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun updateThemeStatusText(isDarkMode: Boolean) {
        binding.tvThemeStatus.text = if (isDarkMode) "On" else "Off"
    }

    private fun updateThemeIcon(isDarkMode: Boolean) {
        binding.ivThemeIcon.setImageResource(
            if (isDarkMode) R.drawable.ic_dark_mode else R.drawable.ic_light_mode
        )
    }

    private fun setupClickListeners() {
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_editProfileFragment)
        }

        binding.btnChangePassword.setOnClickListener {
            // Navigate to security/change password page
            findNavController().navigate(R.id.action_settingsFragment_to_securityFragment)
        }

        binding.btnLogout.setOnClickListener {
            // Clear user session and navigate to login
            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
        }
    }
}
