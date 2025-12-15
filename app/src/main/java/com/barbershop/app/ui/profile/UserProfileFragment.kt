package com.barbershop.app.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.barbershop.app.R
import com.barbershop.app.databinding.FragmentUserProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private lateinit var binding: FragmentUserProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserProfileBinding.bind(view)

        setupClickListeners()
        loadUserData()
    }

    private fun setupClickListeners() {
        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(context, "Edit Profile", Toast.LENGTH_SHORT).show()
        }

        binding.btnBookingHistory.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_bookingListFragment)
        }

        binding.btnFavorites.setOnClickListener {
            Toast.makeText(context, "Favorite Barbers", Toast.LENGTH_SHORT).show()
        }

        binding.btnSettings.setOnClickListener {
            Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
        }

        binding.btnHelp.setOnClickListener {
            Toast.makeText(context, "Help & Support", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogout.setOnClickListener {
            // Navigate back to login
            findNavController().navigate(R.id.action_userProfileFragment_to_loginFragment)
        }
    }

    private fun loadUserData() {
        // Mock data - replace with actual user data from ViewModel
        binding.tvUserName.text = "Alex Johnson"
        binding.tvUserPhone.text = "+1 202 555 0192"
        binding.tvPoints.text = "450 Points"
        binding.tvRewardProgress.text = "50 points until next free trim!"
        binding.progressRewards.progress = 90
    }
}
