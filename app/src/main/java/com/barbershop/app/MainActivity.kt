package com.barbershop.app

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.barbershop.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    companion object {
        private const val PREFS_NAME = "theme_prefs"
        private const val KEY_IS_DARK_MODE = "is_dark_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Apply saved theme before setting content view
        applySavedTheme()
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupToolbar()
    }

    private fun applySavedTheme() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean(KEY_IS_DARK_MODE, false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES 
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_profile -> {
                    showProfileMenu()
                    true
                }
                else -> false
            }
        }
    }

    private fun showProfileMenu() {
        val profileMenuItem = binding.toolbar.findViewById<View>(R.id.action_profile)
        val popupMenu = PopupMenu(this, profileMenuItem)
        popupMenu.menuInflater.inflate(R.menu.profile_popup_menu, popupMenu.menu)
        
        // Update the theme toggle text based on current mode
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean(KEY_IS_DARK_MODE, false)
        popupMenu.menu.findItem(R.id.action_toggle_theme)?.title = 
            if (isDarkMode) "☀️ Light Mode" else "🌙 Dark Mode"
        
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_my_profile -> {
                    navController.navigate(R.id.action_global_userProfileFragment)
                    true
                }
                R.id.action_toggle_theme -> {
                    toggleTheme()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun toggleTheme() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean(KEY_IS_DARK_MODE, false)
        val newMode = !isDarkMode
        
        prefs.edit().putBoolean(KEY_IS_DARK_MODE, newMode).apply()
        
        AppCompatDelegate.setDefaultNightMode(
            if (newMode) AppCompatDelegate.MODE_NIGHT_YES 
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        // Handle back button in toolbar
        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    binding.appBarLayout.visibility = View.GONE
                    binding.bottomNav.visibility = View.GONE
                }
                R.id.homeFragment, R.id.bookingListFragment, R.id.aiFeaturesFragment -> {
                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.navigationIcon = null
                    
                    // Update toolbar title based on destination
                    binding.tvToolbarTitle.text = when (destination.id) {
                        R.id.homeFragment -> getString(R.string.app_name)
                        R.id.bookingListFragment -> "Bookings"
                        R.id.aiFeaturesFragment -> "AI Studio"
                        else -> getString(R.string.app_name)
                    }
                }
                else -> {
                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.bottomNav.visibility = View.VISIBLE
                    // Show back button for sub-pages
                    binding.toolbar.setNavigationIcon(R.drawable.ic_back)
                    
                    binding.tvToolbarTitle.text = when (destination.id) {
                        R.id.userProfileFragment -> "My Profile"
                        R.id.barberProfileFragment -> "Barber Details"
                        R.id.bookingFragment -> "Book Appointment"
                        R.id.editProfileFragment -> "Edit Profile"
                        else -> getString(R.string.app_name)
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
