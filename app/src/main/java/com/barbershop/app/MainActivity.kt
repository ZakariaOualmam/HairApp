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
        
        // Profile button click - show popup menu
        binding.btnProfile.setOnClickListener {
            showProfileMenu()
        }
    }

    private fun showProfileMenu() {
        val popupMenu = PopupMenu(this, binding.btnProfile)
        popupMenu.menuInflater.inflate(R.menu.profile_popup_menu, popupMenu.menu)
        
        // Update the theme toggle text based on current mode
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean(KEY_IS_DARK_MODE, false)
        popupMenu.menu.findItem(R.id.action_toggle_theme)?.title = 
            if (isDarkMode) "☀️ Light Mode" else "🌙 Dark Mode"
        
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_settings -> {
                    // Navigate to Edit Profile (Settings)
                    navController.navigate(R.id.action_global_editProfileFragment)
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

        // Handle back button in toolbar. For Bookings/AI (bottom-nav screens) treat
        // the navigation as a 'go to Home' action; otherwise perform normal navigateUp().
            // We'll set toolbar navigation behavior per-destination below so fragments
            // can't override a global handler and leave it in an inconsistent state.

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    binding.appBarLayout.visibility = View.GONE
                    binding.bottomNav.visibility = View.GONE
                }
                R.id.homeFragment, R.id.bookingListFragment, R.id.aiFeaturesFragment -> {
                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.bottomNav.visibility = View.VISIBLE

                    // For home show profile button and app name.
                    // For Bookings / AI Studio we want the activity toolbar to show
                    // the screen title and a back button aligned like the activity
                    // header — so we show the navigation icon and set the title.
                    if (destination.id == R.id.homeFragment) {
                        binding.btnProfile.visibility = View.VISIBLE
                        binding.toolbar.navigationIcon = null
                        binding.tvToolbarTitle.text = getString(R.string.app_name)
                        binding.toolbar.setNavigationOnClickListener(null)
                    } else if (destination.id == R.id.bookingListFragment) {
                        binding.btnProfile.visibility = View.GONE
                        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
                        binding.tvToolbarTitle.text = "Bookings"
                        binding.toolbar.setNavigationOnClickListener {
                            navController.navigate(R.id.action_global_homeFragment)
                        }
                    } else if (destination.id == R.id.aiFeaturesFragment) {
                        binding.btnProfile.visibility = View.GONE
                        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
                        binding.tvToolbarTitle.text = "AI Studio"
                        binding.toolbar.setNavigationOnClickListener {
                            navController.navigate(R.id.action_global_homeFragment)
                        }
                    }
                }
                else -> {
                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.bottomNav.visibility = View.VISIBLE
                    // Default: show back button for sub-pages
                    binding.toolbar.setNavigationIcon(R.drawable.ic_back)
                    binding.btnProfile.visibility = View.GONE

                    // For Barber Profile show the activity toolbar back icon and title
                    // (the fragment content will not duplicate the header anymore).
                    if (destination.id == R.id.barberProfileFragment) {
                        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
                        binding.tvToolbarTitle.text = "Barber Details"
                        binding.toolbar.setNavigationOnClickListener {
                            navController.navigateUp()
                        }
                    } else {
                        binding.toolbar.setNavigationOnClickListener {
                            navController.navigateUp()
                        }
                        binding.tvToolbarTitle.text = when (destination.id) {
                            R.id.userProfileFragment -> "My Profile"
                            R.id.bookingFragment -> "Book Appointment"
                            R.id.editProfileFragment -> "Settings"
                            else -> getString(R.string.app_name)
                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
