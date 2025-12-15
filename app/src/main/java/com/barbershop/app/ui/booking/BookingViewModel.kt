package com.barbershop.app.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barbershop.app.domain.model.Booking
import com.barbershop.app.domain.repository.BookingRepository
import com.barbershop.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _bookingState = MutableStateFlow<Resource<String>?>(null)
    val bookingState: StateFlow<Resource<String>?> = _bookingState

    fun createBooking(userId: String, barberId: String, barberName: String, serviceName: String, price: Double, timestamp: Long) {
        val booking = Booking(
            id = UUID.randomUUID().toString(),
            userId = userId,
            barberId = barberId,
            barberName = barberName,
            serviceName = serviceName,
            price = price,
            timestamp = timestamp,
            status = "PENDING"
        )
        viewModelScope.launch {
            _bookingState.value = Resource.Loading()
            _bookingState.value = bookingRepository.createBooking(booking)
        }
    }
}
