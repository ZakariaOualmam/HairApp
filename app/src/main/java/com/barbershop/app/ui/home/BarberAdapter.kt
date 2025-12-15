package com.barbershop.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.barbershop.app.databinding.ItemBarberBinding
import com.barbershop.app.domain.model.Barber

class BarberAdapter(private val onItemClick: (Barber) -> Unit) : RecyclerView.Adapter<BarberAdapter.BarberViewHolder>() {

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
            tvBarberName.text = barber.name // Changed property to name to match ID
            tvBarberAddress.text = barber.address
            tvRating.text = "${barber.rating}" // Simplified rating text
            
            Glide.with(root)
                .load(barber.imageUrl)
                .centerCrop()
                .into(ivBarberImage)

            root.setOnClickListener {
                onItemClick(barber)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size
}
