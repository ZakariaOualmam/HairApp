package com.barbershop.app.domain.model

data class Booking(
    val id: String,
    val userId: String,
    val barberId: String,
    val barberName: String,
    val serviceName: String,
    val price: Double,
    val timestamp: Long,
    val status: String // PENDING, CONFIRMED, COMPLETED, CANCELLED
)
