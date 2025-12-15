package com.barbershop.app.data.repository

import com.barbershop.app.domain.model.User
import com.barbershop.app.domain.repository.AuthRepository
import com.barbershop.app.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockAuthRepository @Inject constructor() : AuthRepository {

    private var currentUser: User? = null

    override fun login(email: String, password: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        delay(1000) // Simulate network
        if (password.length > 3) {
            val user = User("1", "John Doe", email, "0123456789")
            currentUser = user
            emit(Resource.Success(user))
        } else {
            emit(Resource.Error("Password must be > 3 chars (Mock)"))
        }
    }

    override fun register(name: String, email: String, password: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        delay(1000)
        val user = User("1", name, email, "0123456789")
        currentUser = user
        emit(Resource.Success(user))
    }

    override fun getCurrentUser(): User? {
        return currentUser
    }

    override fun logout() {
        currentUser = null
    }
}
