package com.barbershop.app.ui.barber

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barbershop.app.databinding.ItemServiceBinding
import com.barbershop.app.domain.model.Service

class ServiceAdapter(private val onServiceSelected: (Service) -> Unit) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    private var services = listOf<Service>()
    private var selectedPosition = -1

    inner class ServiceViewHolder(val binding: ItemServiceBinding) : RecyclerView.ViewHolder(binding.root)

    fun submitList(list: List<Service>) {
        services = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.binding.apply {
            tvServiceName.text = service.name
            tvDuration.text = "${service.durationMinutes} mins"
            tvPrice.text = "$${service.price}"
            
            cbSelect.isChecked = position == selectedPosition
            
            root.setOnClickListener {
                selectedPosition = holder.adapterPosition
                notifyDataSetChanged()
                onServiceSelected(service)
            }
            cbSelect.setOnClickListener {
                selectedPosition = holder.adapterPosition
                notifyDataSetChanged()
                onServiceSelected(service)
            }
        }
    }

    override fun getItemCount() = services.size
}
