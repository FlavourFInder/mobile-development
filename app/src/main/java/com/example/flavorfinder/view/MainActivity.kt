package com.example.flavorfinder.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ActivityMainBinding
import com.example.flavorfinder.view.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchBar.inflateMenu(R.menu.search_bar_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                true
            }
        }



//        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
}