package com.barbershop.app.ui.barber

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBarberProfileBinding.bind(view)

        // Argument parsing manually for now since we don't have safe-args plugin configured fully in this script flow
        // In real app, we would use args.barberId
        val barber = arguments?.getParcelable<Barber>("barber")
        
        if (barber != null) {
            setupUI(barber)
        } else {
            Toast.makeText(context, "Error loading barber", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI(barber: Barber) {
        binding.tvBarberTitle.text = barber.shopName
        binding.tvProfileAddress.text = barber.address

        // Return/up button (inline header)
        binding.btnReturnHome.setOnClickListener {
            findNavController().navigateUp()
        }
        
        Glide.with(this).load(barber.imageUrl).into(binding.ivBarberHeader)
        
        serviceAdapter = ServiceAdapter { service ->
            selectedService = service
            binding.btnBook.isEnabled = true
            binding.btnBook.text = "Book ${service.name} ($${service.price})"
        }
        
        binding.rvServices.apply {
            adapter = serviceAdapter
            layoutManager = LinearLayoutManager(context)
        }
        
        serviceAdapter.submitList(barber.services)
        
        binding.btnBook.setOnClickListener {
            val bundle = Bundle().apply {
                putString("barberId", barber.id)
                putString("barberName", barber.shopName)
                putString("serviceName", selectedService?.name)
                putDouble("price", selectedService?.price ?: 0.0)
            }
            findNavController().navigate(R.id.action_barberProfileFragment_to_bookingFragment, bundle)
        }
    }
}
