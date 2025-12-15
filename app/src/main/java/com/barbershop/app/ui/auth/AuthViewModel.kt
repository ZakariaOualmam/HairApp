package com.barbershop.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barbershop.app.domain.model.User
import com.barbershop.app.domain.repository.AuthRepository
import com.barbershop.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<Resource<User>?>(null)
    val authState: StateFlow<Resource<User>?> = _authState

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            authRepository.login(email, pass).collect {
                _authState.value = it
            }
        }
    }

    fun register(name: String, email: String, pass: String) {
        viewModelScope.launch {
            authRepository.register(name, email, pass).collect {
                _authState.value = it
            }
        }
    }

    fun resetState() {
        _authState.value = null
    }
}
