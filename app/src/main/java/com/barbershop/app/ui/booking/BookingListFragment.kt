package com.barbershop.app.ui.booking

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.barbershop.app.R
import dagger.hilt.android.AndroidEntryPoint

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.barbershop.app.databinding.FragmentBookingListBinding

@AndroidEntryPoint
class BookingListFragment : Fragment(R.layout.fragment_booking_list) {

    private var showingUpcoming = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBookingListBinding.bind(view)

        // Tab click handling
        binding.tabUpcoming.setOnClickListener {
            showingUpcoming = true
            updateTabs(binding)
            binding.rvBookings.adapter?.notifyDataSetChanged()
        }

        binding.tabHistory.setOnClickListener {
            showingUpcoming = false
            updateTabs(binding)
            binding.rvBookings.adapter?.notifyDataSetChanged()
        }

        // Mock Adapter
        binding.rvBookings.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
                return object : RecyclerView.ViewHolder(v) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val statusView = holder.itemView.findViewById<TextView>(R.id.tvStatus)
                
                if (showingUpcoming) {
                    val status = if (position == 0) "Confirmed" else "Pending"
                    statusView.text = status
                    statusView.setTextColor(
                        if (position == 0) android.graphics.Color.parseColor("#10B981")
                        else android.graphics.Color.parseColor("#F59E0B")
                    )
                } else {
                    statusView.text = "Completed"
                    statusView.setTextColor(android.graphics.Color.parseColor("#6B7280"))
                }

                // Card click for details
                holder.itemView.setOnClickListener {
                    Toast.makeText(holder.itemView.context, "View booking details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun getItemCount() = if (showingUpcoming) 3 else 5
        }
    }

    private fun updateTabs(binding: FragmentBookingListBinding) {
        if (showingUpcoming) {
            binding.tabUpcoming.setTextColor(resources.getColor(R.color.primary, null))
            binding.tabUpcoming.setBackgroundResource(R.drawable.bg_tab_indicator_active)
            binding.tabHistory.setTextColor(resources.getColor(R.color.text_secondary, null))
            binding.tabHistory.background = null
        } else {
            binding.tabHistory.setTextColor(resources.getColor(R.color.primary, null))
            binding.tabHistory.setBackgroundResource(R.drawable.bg_tab_indicator_active)
            binding.tabUpcoming.setTextColor(resources.getColor(R.color.text_secondary, null))
            binding.tabUpcoming.background = null
        }
    }
}
