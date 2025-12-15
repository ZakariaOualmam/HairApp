package com.barbershop.app.ui.home

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
class HomeViewModel @Inject constructor(
    private val barberRepository: BarberRepository
) : ViewModel() {

    private val _barbersState = MutableStateFlow<Resource<List<Barber>>>(Resource.Loading())
    val barbersState: StateFlow<Resource<List<Barber>>> = _barbersState

    init {
        loadBarbers()
    }

    private fun loadBarbers() {
        viewModelScope.launch {
            _barbersState.value = Resource.Loading()
            _barbersState.value = barberRepository.getBarbers()
        }
    }
}
