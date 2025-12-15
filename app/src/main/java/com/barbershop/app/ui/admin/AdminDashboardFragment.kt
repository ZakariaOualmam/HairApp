package com.barbershop.app.ui.admin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.barbershop.app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminDashboardFragment : Fragment(R.layout.fragment_admin_dashboard) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Logic for admin dashboard would go here
    }
}
