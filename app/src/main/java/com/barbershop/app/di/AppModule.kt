package com.barbershop.app.di

import com.barbershop.app.data.repository.MockAuthRepository
import com.barbershop.app.data.repository.MockBarberRepository
import com.barbershop.app.data.repository.MockBookingRepository
import com.barbershop.app.domain.repository.AuthRepository
import com.barbershop.app.domain.repository.BarberRepository
import com.barbershop.app.domain.repository.BookingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        mockAuthRepository: MockAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindBarberRepository(
        mockBarberRepository: MockBarberRepository
    ): BarberRepository

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        mockBookingRepository: MockBookingRepository
    ): BookingRepository
}
