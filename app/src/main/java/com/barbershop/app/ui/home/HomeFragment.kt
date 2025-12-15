package com.barbershop.app.ui.home

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
        observeBarbers()
    }

    private fun setupRecyclerView() {
        barberAdapter = BarberAdapter { barber ->
            val bundle = Bundle().apply {
                putParcelable("barber", barber)
            }
            findNavController().navigate(R.id.action_homeFragment_to_barberProfileFragment, bundle)
        }
        
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
        
        // Filter chips
        binding.chipNearMe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "Showing nearby barbers", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.chipRating.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "Filtering by rating 4.5+", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.chipOpenNow.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "Showing open barbershops", Toast.LENGTH_SHORT).show()
            }
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
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
