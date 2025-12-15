package com.barbershop.app.domain.repository

import com.barbershop.app.domain.model.User
import com.barbershop.app.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Resource<User>>
    fun register(name: String, email: String, password: String): Flow<Resource<User>>
    fun getCurrentUser(): User?
    fun logout()
}
