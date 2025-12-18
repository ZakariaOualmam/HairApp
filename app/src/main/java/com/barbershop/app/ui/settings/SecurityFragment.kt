package com.barbershop.app.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.barbershop.app.R
import com.barbershop.app.databinding.FragmentSecurityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecurityFragment : Fragment(R.layout.fragment_security) {

    private lateinit var binding: FragmentSecurityBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSecurityBinding.bind(view)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnUpdatePassword.setOnClickListener {
            updatePassword()
        }
    }

    private fun updatePassword() {
        val currentPassword = binding.etCurrentPassword.text.toString()
        val newPassword = binding.etNewPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        // Validation
        if (currentPassword.isEmpty()) {
            binding.etCurrentPassword.error = "Current password is required"
            return
        }

        if (newPassword.isEmpty()) {
            binding.etNewPassword.error = "New password is required"
            return
        }

        if (newPassword.length < 6) {
            binding.etNewPassword.error = "Password must be at least 6 characters"
            return
        }

        if (confirmPassword.isEmpty()) {
            binding.etConfirmPassword.error = "Please confirm your password"
            return
        }

        if (newPassword != confirmPassword) {
            binding.etConfirmPassword.error = "Passwords don't match"
            return
        }

        // TODO: Call API to update password
        Toast.makeText(context, "Password updated successfully!", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }
}
