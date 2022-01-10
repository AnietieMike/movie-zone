package com.anietie.moviezone

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.anietie.moviezone.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

//    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handles the splash screen transition if any.
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView = binding.bottomNavView
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        onDestinationChangedListener()
    }

    private fun onDestinationChangedListener() {
        try {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                bottomNavigationView.visibility = View.VISIBLE
                when (destination.id) {
                    R.id.movieDetailsFragment -> {
                        bottomNavigationView.visibility = View.GONE
                    }
                    else -> {
                        bottomNavigationView.visibility = View.VISIBLE
                    }
                }
            }
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }
}
