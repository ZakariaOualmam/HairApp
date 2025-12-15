package com.barbershop.app.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.barbershop.app.R
import com.barbershop.app.domain.model.Barber

class BarberSuggestionAdapter(
    private val onItemClick: (Barber) -> Unit
) : RecyclerView.Adapter<BarberSuggestionAdapter.SuggestionViewHolder>() {

    private var suggestions: List<Barber> = emptyList()

    fun submitList(list: List<Barber>) {
        suggestions = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_barber_suggestion, parent, false)
        return SuggestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val barber = suggestions[position]
        holder.tvName.text = barber.name
        Glide.with(holder.ivProfile)
            .load(barber.imageUrl)
            .placeholder(R.drawable.ic_profile)
            .circleCrop()
            .into(holder.ivProfile)
        holder.itemView.setOnClickListener { onItemClick(barber) }
    }

    override fun getItemCount(): Int = suggestions.size

    class SuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfile: ImageView = itemView.findViewById(R.id.ivProfile)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
    }
}
