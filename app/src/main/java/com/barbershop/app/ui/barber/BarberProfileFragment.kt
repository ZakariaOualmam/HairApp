package com.barbershop.app.ui.barber

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.barbershop.app.R
import com.barbershop.app.databinding.FragmentBarberProfileBinding
import com.barbershop.app.domain.model.Barber
import com.barbershop.app.domain.model.Service
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarberProfileFragment : Fragment(R.layout.fragment_barber_profile) {

    private lateinit var binding: FragmentBarberProfileBinding
    private lateinit var serviceAdapter: ServiceAdapter
    private var selectedService: Service? = null
    private var selectedPaymentMethod: String = "cash" // Default to cash
    private var currentBarber: Barber? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBarberProfileBinding.bind(view)

        // Argument parsing manually for now since we don't have safe-args plugin configured fully in this script flow
        // In real app, we would use args.barberId
        val barber = arguments?.getParcelable<Barber>("barber")
        
        if (barber != null) {
            currentBarber = barber
            setupUI(barber)
            setupPaymentMethods()
            setupActionButtons(barber)
        } else {
            Toast.makeText(context, "Error loading barber", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI(barber: Barber) {
        // Use activity toolbar for title and navigation
        requireActivity().title = barber.shopName
        
        // Set barber info
        binding.tvBarberName.text = barber.shopName
        binding.tvBarberRating.text = String.format("%.1f", barber.rating)
        binding.tvStatusMessage.text = "${barber.shopName} accepted your request"
        
        // Load barber avatar image
        Glide.with(this)
            .load(barber.imageUrl)
            .placeholder(R.drawable.ic_profile)
            .into(binding.ivBarberAvatar)
        
        // Set default distance/time/price info (would be calculated from location in real app)
        binding.tvDistance.text = "1.8 Km"
        binding.tvTime.text = "18 Min"
        binding.tvPrice.text = "$${barber.services.firstOrNull()?.price?.toInt() ?: 25}"
        
        // Generate a barber ID badge
        binding.tvBarberId.text = "BRB${barber.id.take(6).uppercase()}"
        
        // Setup services list
        serviceAdapter = ServiceAdapter { service ->
            selectedService = service
            // Update price when service selected
            binding.tvPrice.text = "$${service.price.toInt()}"
        }
        
        binding.rvServices.apply {
            adapter = serviceAdapter
            layoutManager = LinearLayoutManager(context)
        }
        
        serviceAdapter.submitList(barber.services)
    }

    private fun setupPaymentMethods() {
        // Cash payment card click
        binding.cardCashPayment.setOnClickListener {
            selectedPaymentMethod = "cash"
            binding.rbCash.isChecked = true
            binding.rbOnline.isChecked = false
        }
        
        // Online payment card click
        binding.cardOnlinePayment.setOnClickListener {
            selectedPaymentMethod = "online"
            binding.rbCash.isChecked = false
            binding.rbOnline.isChecked = true
        }
        
        // Radio buttons
        binding.rbCash.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedPaymentMethod = "cash"
                binding.rbOnline.isChecked = false
            }
        }
        
        binding.rbOnline.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedPaymentMethod = "online"
                binding.rbCash.isChecked = false
            }
        }
    }

    private fun setupActionButtons(barber: Barber) {
        // Barber (Call) button
        binding.btnCallBarber.setOnClickListener {
            // In a real app, this would get the barber's phone number from profile
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
        
        // Messages button - navigate to chat/messaging
        binding.btnMessages.setOnClickListener {
            // Navigate to a chat screen (would be implemented in full app)
            Toast.makeText(context, "Opening messages with ${barber.shopName}", Toast.LENGTH_SHORT).show()
            // In a real app: findNavController().navigate(R.id.action_barberProfileFragment_to_chatFragment, bundle)
        }
    }
}
