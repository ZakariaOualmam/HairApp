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
    private var isServicesExpanded = false
    private var isBarberDetailsExpanded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBarberProfileBinding.bind(view)

        val barber = arguments?.getParcelable<Barber>("barber")
        
        if (barber != null) {
            currentBarber = barber
            setupUI(barber)
            setupServiceSelection()
            setupBarberProfile(barber)
            setupActionButtons(barber)
            checkLocationPermission()
        } else {
            Toast.makeText(context, "Error loading barber", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI(barber: Barber) {
        requireActivity().title = "Barber Details"
        
        // Set barber info in profile card
        binding.tvBarberName.text = barber.shopName
        binding.tvBarberRating.text = String.format("%.1f", barber.rating)
        binding.tvDistance.text = "1.8 km"
        
        // Set large barber info
        binding.tvBarberNameLarge.text = barber.shopName
        binding.tvBarberRatingLarge.text = "${String.format("%.1f", barber.rating)} (120 reviews)"
        binding.tvBarberId.text = "ID: BRB${barber.id.take(6).uppercase()}"
        binding.tvBarberLocation.text = barber.address
        binding.tvDistanceLarge.text = "1.8 km"
        
        // Load barber avatar
        Glide.with(this)
            .load(barber.imageUrl)
            .placeholder(R.drawable.ic_profile)
            .into(binding.ivBarberAvatar)
        
        Glide.with(this)
            .load(barber.imageUrl)
            .placeholder(R.drawable.ic_profile)
            .into(binding.ivBarberAvatarLarge)
    }

    private fun setupServiceSelection() {
        // Header click to expand/collapse services
        binding.offerHaircutHeader.setOnClickListener {
            toggleServicesSection()
        }
        
        binding.cardOfferHaircut.setOnClickListener {
            toggleServicesSection()
        }
        
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
    
    private fun toggleServicesSection() {
        isServicesExpanded = !isServicesExpanded
        
        if (isServicesExpanded) {
            binding.servicesExpandableSection.visibility = View.VISIBLE
            binding.ivExpandArrow.rotation = 180f
        } else {
            binding.servicesExpandableSection.visibility = View.GONE
            binding.ivExpandArrow.rotation = 0f
        }
    }
    
    private fun selectService(name: String, price: Double, time: String) {
        selectedServiceName = name
        selectedServicePrice = price
        selectedServiceTime = time
        
        // Show selected service badge
        binding.tvSelectedService.text = "$name - $${price.toInt()}"
        binding.tvSelectedService.visibility = View.VISIBLE
        
        // Collapse services section
        isServicesExpanded = false
        binding.servicesExpandableSection.visibility = View.GONE
        binding.ivExpandArrow.rotation = 0f
        
        // Show barber profile card and change button to "Profile"
        binding.cardBarberProfile.visibility = View.VISIBLE
        binding.btnMainAction.text = "View Profile"
        
        // Hide barber details expanded section when selecting new service
        binding.barberDetailsExpanded.visibility = View.GONE
        isBarberDetailsExpanded = false
    }

    private fun setupBarberProfile(barber: Barber) {
        // Barber profile card click - show expanded details
        binding.cardBarberProfile.setOnClickListener {
            showBarberDetails()
        }
        
        // Pick Service button - go back to service selection
        binding.btnPickService.setOnClickListener {
            hideBarberDetails()
            toggleServicesSection()
        }
    }
    
    private fun showBarberDetails() {
        isBarberDetailsExpanded = true
        binding.cardBarberProfile.visibility = View.GONE
        binding.barberDetailsExpanded.visibility = View.VISIBLE
        
        // Show action buttons (Call & Messages)
        binding.barberActionButtons.visibility = View.VISIBLE
        
        // Hide bottom main action button when showing barber details
        binding.bottomButtonContainer.visibility = View.GONE
    }
    
    private fun hideBarberDetails() {
        isBarberDetailsExpanded = false
        binding.barberDetailsExpanded.visibility = View.GONE
        binding.cardBarberProfile.visibility = if (selectedServiceName != null) View.VISIBLE else View.GONE
        
        // Show bottom button again
        binding.bottomButtonContainer.visibility = View.VISIBLE
    }

    private fun setupActionButtons(barber: Barber) {
        // Main action button
        binding.btnMainAction.setOnClickListener {
            if (selectedServiceName != null) {
                // Service is selected - show barber profile details
                showBarberDetails()
            } else {
                // No service selected - prompt to select one
                Toast.makeText(context, "Please select a service first", Toast.LENGTH_SHORT).show()
                toggleServicesSection()
            }
        }
        
        // Call button
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
        
        // Messages button
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
            binding.ivUserPin.visibility = View.GONE
        } else {
            binding.cardLocationWarning.visibility = View.GONE
            binding.ivUserPin.visibility = View.VISIBLE
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
