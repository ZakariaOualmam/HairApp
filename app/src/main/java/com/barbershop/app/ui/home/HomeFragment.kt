package com.barbershop.app.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.barbershop.app.R
import com.barbershop.app.databinding.FragmentHomeBinding
import com.barbershop.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var barberAdapter: BarberAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        setupRecyclerView()
        setupClickListeners()
        setupMapSection()
        observeBarbers()
    }

    private fun setupRecyclerView() {
        barberAdapter = BarberAdapter(
            onItemClick = { barber ->
                val bundle = Bundle().apply {
                    putParcelable("barber", barber)
                }
                findNavController().navigate(R.id.action_homeFragment_to_barberProfileFragment, bundle)
            }
        )
        
        binding.rvBarbers.apply {
            adapter = barberAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClickListeners() {
        // Filter icon
        binding.ivFilter.setOnClickListener {
            Toast.makeText(context, "Filter options coming soon", Toast.LENGTH_SHORT).show()
        }
        
        // Search functionality
        binding.etSearch.setOnEditorActionListener { textView, _, _ ->
            val query = textView.text.toString()
            if (query.isNotEmpty()) {
                Toast.makeText(context, "Searching for: $query", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun setupMapSection() {
        // Map card click - open Google Maps with all barber locations
        binding.mapCard.setOnClickListener {
            openGoogleMapsWithBarbers()
        }
        
        // Open Map button
        binding.btnOpenMap.setOnClickListener {
            openGoogleMapsWithBarbers()
        }
    }

    private fun openGoogleMapsWithBarbers() {
        // Default location (can be replaced with user's current location)
        val centerLat = 40.7128
        val centerLng = -74.0060
        
        // Open Google Maps centered on the area with barbers
        val uri = Uri.parse("geo:$centerLat,$centerLng?q=barbershop")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            // Fallback to browser
            val browserUri = Uri.parse("https://www.google.com/maps/search/barbershop/@$centerLat,$centerLng,14z")
            startActivity(Intent(Intent.ACTION_VIEW, browserUri))
        }
    }

    private fun observeBarbers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.barbersState.collectLatest { state ->
                when (state) {
                    is Resource.Loading -> {
                        // Show loading state
                    }
                    is Resource.Success -> {
                        val barbers = state.data ?: emptyList()
                        barberAdapter.submitList(barbers)
                        // Update map barber count
                        binding.tvMapBarberCount.text = "${barbers.size} barbers nearby"
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
