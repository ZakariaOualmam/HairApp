package com.barbershop.app.ui.barber

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.barbershop.app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarberDashboardFragment : Fragment(R.layout.fragment_barber_dashboard) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Logic for barber dashboard would go here
    }
}
