package com.barbershop.app.domain.repository

import com.barbershop.app.domain.model.Barber
import com.barbershop.app.utils.Resource
import kotlinx.coroutines.flow.Flow

interface BarberRepository {
    suspend fun getBarbers(): Resource<List<Barber>>
    suspend fun getBarberById(id: String): Resource<Barber>
    suspend fun searchBarbers(query: String): Resource<List<Barber>>
}
