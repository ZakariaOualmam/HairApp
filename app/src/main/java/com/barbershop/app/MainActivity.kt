package com.barbershop.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.barbershop.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        setSupportActionBar(binding.toolbar)
        NavigationUI.setupWithNavController(binding.toolbar, navController)

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            NavigationUI.onNavDestinationSelected(menuItem, navController)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    binding.appBarLayout.visibility = View.GONE
                    binding.bottomNav.visibility = View.GONE
                }
                else -> {
                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.bottomNav.visibility = View.VISIBLE
                }
            }
        }
    }
}
