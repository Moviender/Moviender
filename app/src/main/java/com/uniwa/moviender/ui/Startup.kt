package com.uniwa.moviender.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.ActivityStartupBinding

class Startup : AppCompatActivity() {

    private lateinit var binding: ActivityStartupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        // TODO execute when reached friends fragment
        val navView: BottomNavigationView = binding.navView

        // Workaround for the FragmentContainerView bug
        // https://issuetracker.google.com/issues/142847973
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.startup_nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener {_, destination, _ ->
            navView.visibility = when(destination.id) {
                R.id.navigation_friends, R.id.navigation_movies -> View.VISIBLE
                else -> View.GONE
            }
        }
        navView.setOnItemSelectedListener {
            if (it.itemId == R.id.navigation_friends) {
                navController.popBackStack(R.id.navigation_friends, false)
                true
            }
            else {
                NavigationUI.onNavDestinationSelected(it, navController)
            }
        }
    }
}