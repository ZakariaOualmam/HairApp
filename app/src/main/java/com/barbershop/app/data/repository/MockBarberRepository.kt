package com.barbershop.app.data.repository

import com.barbershop.app.domain.model.Barber
import com.barbershop.app.domain.model.Service
import com.barbershop.app.domain.repository.BarberRepository
import com.barbershop.app.utils.Resource
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockBarberRepository @Inject constructor() : BarberRepository {

    private val services = listOf(
        Service("s1", "Haircut", 20.0, 30),
        Service("s2", "Beard Trim", 15.0, 20),
        Service("s3", "Full Shave", 25.0, 45)
    )

    private val barbers = listOf(
        Barber(
            "1", "Ahmed Barber", "Ahmed's Shop", "123 Main St", 4.8, 120,
            "https://i.pravatar.cc/300?img=1", services, 34.0, -118.0
        ),
        Barber(
            "2", "Cool Cuts", "Cuts Studio", "456 Market Ave", 4.5, 85,
            "https://i.pravatar.cc/300?img=3", services, 34.01, -118.01
        ),
        Barber(
            "3", "Top Style", "Style Hub", "789 Broadway", 4.9, 230,
            "https://i.pravatar.cc/300?img=12", services, 34.02, -118.02
        )
    )

    override suspend fun getBarbers(): Resource<List<Barber>> {
        delay(500)
        return Resource.Success(barbers)
    }

    override suspend fun getBarberById(id: String): Resource<Barber> {
        delay(300)
        val barber = barbers.find { it.id == id }
        return if (barber != null) Resource.Success(barber) else Resource.Error("Barber not found")
    }

    override suspend fun searchBarbers(query: String): Resource<List<Barber>> {
        delay(300)
        val filtered = barbers.filter { it.name.contains(query, ignoreCase = true) || it.shopName.contains(query, ignoreCase = true) }
        return Resource.Success(filtered)
    }
}
