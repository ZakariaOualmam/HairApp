package com.barbershop.app.domain.repository

import com.barbershop.app.domain.model.Booking
import com.barbershop.app.utils.Resource
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    suspend fun createBooking(booking: Booking): Resource<String>
    fun getUserBookings(userId: String): Flow<Resource<List<Booking>>>
}
