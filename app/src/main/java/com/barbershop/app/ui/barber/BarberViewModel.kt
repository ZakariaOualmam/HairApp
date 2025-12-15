package com.barbershop.app.ui.barber

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barbershop.app.domain.model.Barber
import com.barbershop.app.domain.repository.BarberRepository
import com.barbershop.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarberViewModel @Inject constructor(
    private val repository: BarberRepository
) : ViewModel() {

    private val _barberState = MutableStateFlow<Resource<Barber>?>(null)
    val barberState: StateFlow<Resource<Barber>?> = _barberState

    fun loadBarber(id: String) {
        viewModelScope.launch {
            _barberState.value = Resource.Loading()
            _barberState.value = repository.getBarberById(id)
        }
    }
}
