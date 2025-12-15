package com.barbershop.app.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.barbershop.app.R
import com.barbershop.app.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private lateinit var binding: FragmentEditProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditProfileBinding.bind(view)

        loadUserData()
        setupClickListeners()
    }

    private fun loadUserData() {
        // Load from SharedPreferences or ViewModel
        // Mock data for now
        binding.etFullName.setText("Alex Johnson")
        binding.etEmail.setText("alex.johnson@email.com")
        binding.etPhone.setText("+1 202 555 0192")
        binding.etLocation.setText("New York, NY")
    }

    private fun setupClickListeners() {
        binding.btnChangePhoto.setOnClickListener {
            Toast.makeText(context, "Photo picker coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.btnSaveChanges.setOnClickListener {
            saveChanges()
        }
    }

    private fun saveChanges() {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val currentPassword = binding.etCurrentPassword.text.toString()
        val newPassword = binding.etNewPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        // Validation
        if (fullName.isEmpty()) {
            binding.etFullName.error = "Name is required"
            return
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Valid email is required"
            return
        }

        if (phone.isEmpty()) {
            binding.etPhone.error = "Phone is required"
            return
        }

        // Password change validation
        if (newPassword.isNotEmpty()) {
            if (currentPassword.isEmpty()) {
                binding.etCurrentPassword.error = "Current password required"
                return
            }
            if (newPassword.length < 6) {
                binding.etNewPassword.error = "Password must be at least 6 characters"
                return
            }
            if (newPassword != confirmPassword) {
                binding.etConfirmPassword.error = "Passwords don't match"
                return
            }
        }

        // Save to SharedPreferences or API
        Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }
}
