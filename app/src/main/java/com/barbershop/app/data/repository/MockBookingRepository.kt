package com.barbershop.app.data.repository

import com.barbershop.app.domain.model.Booking
import com.barbershop.app.domain.repository.BookingRepository
import com.barbershop.app.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockBookingRepository @Inject constructor() : BookingRepository {

    private val bookings = mutableListOf<Booking>()

    override suspend fun createBooking(booking: Booking): Resource<String> {
        delay(500)
        bookings.add(booking)
        return Resource.Success(booking.id)
    }

    override fun getUserBookings(userId: String): Flow<Resource<List<Booking>>> = flow {
        delay(500)
        val userBookings = bookings.filter { it.userId == userId }
        emit(Resource.Success(userBookings))
    }
}
