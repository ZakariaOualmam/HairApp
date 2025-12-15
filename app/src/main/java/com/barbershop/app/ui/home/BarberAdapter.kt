package com.barbershop.app.ui.home

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.barbershop.app.databinding.ItemBarberBinding
import com.barbershop.app.domain.model.Barber

class BarberAdapter(
    private val onItemClick: (Barber) -> Unit,
    private val onMapClick: ((Barber) -> Unit)? = null
) : RecyclerView.Adapter<BarberAdapter.BarberViewHolder>() {

    inner class BarberViewHolder(val binding: ItemBarberBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Barber>() {
        override fun areItemsTheSame(oldItem: Barber, newItem: Barber): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Barber, newItem: Barber): Boolean = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Barber>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarberViewHolder {
        val binding = ItemBarberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BarberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BarberViewHolder, position: Int) {
        val barber = differ.currentList[position]
        holder.binding.apply {
            tvBarberName.text = barber.name
            tvBarberAddress.text = "${barber.address} • ${String.format("%.1f", barber.distance ?: 0.0)} mi"
            tvRating.text = "${barber.rating}"
            tvSpecialty.text = barber.specialties?.joinToString(" • ") ?: "Haircut • Styling"
            
            Glide.with(root)
                .load(barber.imageUrl)
                .centerCrop()
                .into(ivBarberImage)

            root.setOnClickListener {
                onItemClick(barber)
            }

            btnBookNow.setOnClickListener {
                onItemClick(barber)
            }

            btnViewMap.setOnClickListener {
                if (onMapClick != null) {
                    onMapClick.invoke(barber)
                } else {
                    // Default: Open in Google Maps
                    val context = root.context
                    val latitude = barber.latitude ?: 0.0
                    val longitude = barber.longitude ?: 0.0
                    val uri = if (latitude != 0.0 && longitude != 0.0) {
                        Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude(${barber.name})")
                    } else {
                        Uri.parse("geo:0,0?q=${Uri.encode(barber.address)}")
                    }
                    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    
                    if (mapIntent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(mapIntent)
                    } else {
                        // Fallback: open in browser
                        val browserUri = Uri.parse("https://maps.google.com/?q=${Uri.encode(barber.address)}")
                        context.startActivity(Intent(Intent.ACTION_VIEW, browserUri))
                    }
                }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size
}
