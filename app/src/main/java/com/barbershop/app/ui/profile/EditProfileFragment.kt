package com.barbershop.app.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.barbershop.app.R
import com.barbershop.app.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private lateinit var binding: FragmentEditProfileBinding

    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri = data?.data
            if (uri != null) {
                selectedImageUri = uri
                binding.ivAvatar.setImageURI(uri)
            } else {
                Toast.makeText(context, "Failed to get image", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
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

        // Save to SharedPreferences or API
        Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }
}
