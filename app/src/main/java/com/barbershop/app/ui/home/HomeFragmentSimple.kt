package com.barbershop.app.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.barbershop.app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragmentSimple : Fragment(R.layout.fragment_home_simple) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(context, "Home Screen Loaded!", Toast.LENGTH_SHORT).show()
    }
}
