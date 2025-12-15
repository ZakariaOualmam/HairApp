package com.barbershop.app.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val profileImage: String? = null,
    val loyaltyPoints: Int = 0,
    val isBarber: Boolean = false
)
