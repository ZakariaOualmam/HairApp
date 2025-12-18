package com.barbershop.app.ui.barber

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.barbershop.app.R
import com.barbershop.app.databinding.FragmentBarberProfileBinding
import com.barbershop.app.domain.model.Barber
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarberProfileFragment : Fragment(R.layout.fragment_barber_profile) {

    private lateinit var binding: FragmentBarberProfileBinding
    private var currentBarber: Barber? = null
    private var selectedServiceName: String? = null
    private var selectedServicePrice: Double = 0.0
    private var selectedServiceTime: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBarberProfileBinding.bind(view)

        val barber = arguments?.getParcelable<Barber>("barber")
        
        if (barber != null) {
            currentBarber = barber
            setupBarberInfo(barber)
            setupOfferHaircutCard()
            setupServicePopup()
            setupBarberProfilePopup(barber)
            setupActionButtons(barber)
            checkLocationPermission()
        } else {
            Toast.makeText(context, "Error loading barber", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBarberInfo(barber: Barber) {
        requireActivity().title = "Barber Details"
        
        // Set barber info in popup
        binding.tvBarberName.text = barber.shopName
        binding.tvBarberRating.text = "${String.format("%.1f", barber.rating)} (120 reviews)"
        binding.tvBarberId.text = "ID: BRB${barber.id.take(6).uppercase()}"
        binding.tvBarberLocation.text = barber.address
        binding.tvDistance.text = "1.8 km"
        
        // Load barber avatar
        Glide.with(this)
            .load(barber.imageUrl)
            .placeholder(R.drawable.ic_profile)
            .into(binding.ivBarberAvatar)
    }

    private fun setupOfferHaircutCard() {
        // Click on "Offer your haircut" card to show service popup
        binding.cardOfferHaircut.setOnClickListener {
            showServicePopup()
        }
    }

    private fun setupServicePopup() {
        // Close service popup
        binding.btnCloseServicePopup.setOnClickListener {
            hideServicePopup()
        }
        
        // Click on overlay to close
        binding.servicePopupOverlay.setOnClickListener {
            hideServicePopup()
        }
        
        // Prevent card click from closing popup
        binding.cardServicePopup.setOnClickListener { }
        
        // Service selection clicks
        binding.cardServiceHaircut.setOnClickListener {
            selectService("Haircut", 25.0, "~30 min")
            binding.rbHaircut.isChecked = true
            binding.rbBeardTrim.isChecked = false
            binding.rbFullShave.isChecked = false
        }
        
        binding.cardServiceBeardTrim.setOnClickListener {
            selectService("Beard Trim", 15.0, "~15 min")
            binding.rbHaircut.isChecked = false
            binding.rbBeardTrim.isChecked = true
            binding.rbFullShave.isChecked = false
        }
        
        binding.cardServiceFullShave.setOnClickListener {
            selectService("Full Shave", 20.0, "~20 min")
            binding.rbHaircut.isChecked = false
            binding.rbBeardTrim.isChecked = false
            binding.rbFullShave.isChecked = true
        }
        
        // Radio button clicks
        binding.rbHaircut.setOnClickListener {
            selectService("Haircut", 25.0, "~30 min")
            binding.rbBeardTrim.isChecked = false
            binding.rbFullShave.isChecked = false
        }
        
        binding.rbBeardTrim.setOnClickListener {
            selectService("Beard Trim", 15.0, "~15 min")
            binding.rbHaircut.isChecked = false
            binding.rbFullShave.isChecked = false
        }
        
        binding.rbFullShave.setOnClickListener {
            selectService("Full Shave", 20.0, "~20 min")
            binding.rbHaircut.isChecked = false
            binding.rbBeardTrim.isChecked = false
        }
    }
    
    private fun showServicePopup() {
        binding.servicePopupOverlay.visibility = View.VISIBLE
    }
    
    private fun hideServicePopup() {
        binding.servicePopupOverlay.visibility = View.GONE
    }
    
    private fun selectService(name: String, price: Double, time: String) {
        selectedServiceName = name
        selectedServicePrice = price
        selectedServiceTime = time
        
        // Hide service popup
        hideServicePopup()
        
        // Show selected service badge on card
        binding.tvSelectedService.text = "$name - $${price.toInt()}"
        binding.tvSelectedService.visibility = View.VISIBLE
        
        // Show action buttons (Barber Profile + Command)
        binding.actionButtonsContainer.visibility = View.VISIBLE
    }

    private fun setupBarberProfilePopup(barber: Barber) {
        // Barber profile button click - show barber popup
        binding.btnBarberProfile.setOnClickListener {
            showBarberProfilePopup()
        }
        
        // Close barber popup
        binding.btnCloseBarberPopup.setOnClickListener {
            hideBarberProfilePopup()
        }
        
        // Click on overlay to close
        binding.barberProfileOverlay.setOnClickListener {
            hideBarberProfilePopup()
        }
        
        // Prevent card click from closing popup
        binding.cardBarberProfilePopup.setOnClickListener { }
    }
    
    private fun showBarberProfilePopup() {
        binding.barberProfileOverlay.visibility = View.VISIBLE
    }
    
    private fun hideBarberProfilePopup() {
        binding.barberProfileOverlay.visibility = View.GONE
    }

    private fun setupActionButtons(barber: Barber) {
        // Command button - place the order
        binding.btnCommand.setOnClickListener {
            if (selectedServiceName != null) {
                Toast.makeText(context, "Placing order for $selectedServiceName...", Toast.LENGTH_SHORT).show()
                // Navigate to booking or confirmation screen
                val bundle = Bundle().apply {
                    putString("barberId", barber.id)
                    putString("barberName", barber.shopName)
                    putString("serviceName", selectedServiceName)
                    putDouble("price", selectedServicePrice)
                }
                findNavController().navigate(R.id.action_barberProfileFragment_to_bookingFragment, bundle)
            }
        }
        
        // Call button in barber popup
        binding.btnCallBarber.setOnClickListener {
            val phoneNumber = "tel:+1234567890"
            try {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse(phoneNumber)
                }
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Unable to make call", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Messages button in barber popup
        binding.btnMessages.setOnClickListener {
            Toast.makeText(context, "Opening messages with ${barber.shopName}", Toast.LENGTH_SHORT).show()
        }
        
        // Location warning click - go to settings
        binding.cardLocationWarning.setOnClickListener {
            openLocationSettings()
        }
    }
    
    private fun checkLocationPermission() {
        val hasLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        if (!hasLocationPermission) {
            binding.cardLocationWarning.visibility = View.VISIBLE
            binding.userLocationContainer.visibility = View.GONE
        } else {
            binding.cardLocationWarning.visibility = View.GONE
            binding.userLocationContainer.visibility = View.VISIBLE
        }
    }
    
    private fun openLocationSettings() {
        try {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", requireContext().packageName, null)
            }
            startActivity(intent)
        }
    }
}
            startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", requireContext().packageName, null)
            }
            startActivity(intent)
        }
    }
}
