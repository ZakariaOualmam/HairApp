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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBookingListBinding.bind(view)

        // Mock Adapter
        binding.rvBookings.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
                return object : RecyclerView.ViewHolder(v) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                // Determine mock data based on position
                val statusView = holder.itemView.findViewById<TextView>(R.id.tvStatus)
                val status = if (position == 0) "Confirmed" else "Pending"
                statusView.text = status
                statusView.setTextColor(if (position == 0) android.graphics.Color.parseColor("#4CAF50") else android.graphics.Color.parseColor("#FF9800"))
                statusView.setBackgroundColor(if (position == 0) android.graphics.Color.parseColor("#E8F5E9") else android.graphics.Color.parseColor("#FFF3E0"))
                
                holder.itemView.findViewById<View>(R.id.btnReschedule).setOnClickListener {
                    Toast.makeText(holder.itemView.context, "Reschedule Clicked", Toast.LENGTH_SHORT).show()
                }
                holder.itemView.findViewById<View>(R.id.btnCancel).setOnClickListener {
                    Toast.makeText(holder.itemView.context, "Cancel Clicked", Toast.LENGTH_SHORT).show()
                }
            }

            override fun getItemCount() = 3
        }
    }
}
