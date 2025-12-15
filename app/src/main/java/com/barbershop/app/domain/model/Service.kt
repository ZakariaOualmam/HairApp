package com.barbershop.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Service(
    val id: String,
    val name: String,
    val price: Double,
    val durationMinutes: Int,
    val imageUrl: String? = null
) : Parcelable
