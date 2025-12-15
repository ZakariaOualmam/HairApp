package com.barbershop.app.ui.booking

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.barbershop.app.R
import com.barbershop.app.databinding.FragmentBookingBinding
import com.barbershop.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class BookingFragment : Fragment(R.layout.fragment_booking) {

    private val viewModel: BookingViewModel by viewModels()
    private lateinit var binding: FragmentBookingBinding
    
    // Arguments (Simplified for now, in real app use args)
    private var barberId: String = ""
    private var barberName: String = ""
    private var serviceName: String = ""
    private var price: Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookingBinding.bind(view)

        arguments?.let {
            barberId = it.getString("barberId", "")
            barberName = it.getString("barberName", "")
            serviceName = it.getString("serviceName", "")
            price = it.getDouble("price", 0.0)
        }

        binding.tvBookingDetails.text = "Booking: $serviceName\nAt: $barberName\nPrice: $$price"

        binding.btnConfirmBooking.setOnClickListener {
            val timestamp = binding.calendarView.date
            // Mock userId "1"
            viewModel.createBooking("1", barberId, barberName, serviceName, price, timestamp)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookingState.collectLatest { state ->
                when (state) {
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Booking Confirmed! ID: ${state.data}", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_bookingFragment_to_homeFragment)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    null -> Unit
                }
            }
        }
    }
}
