package com.barbershop.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Barber(
    val id: String,
    val name: String,
    val shopName: String,
    val address: String,
    val rating: Double,
    val reviewCount: Int,
    val imageUrl: String,
    val services: List<Service> = emptyList(),
    val latitude: Double,
    val longitude: Double,
    val isFavorite: Boolean = false
) : Parcelable
