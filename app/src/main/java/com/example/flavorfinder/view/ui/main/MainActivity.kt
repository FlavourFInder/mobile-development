package com.example.flavorfinder.view.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ActivityMainBinding
import com.example.flavorfinder.helper.ViewModelFactory
import com.example.flavorfinder.pref.UserPreference
import com.example.flavorfinder.pref.dataStore
import com.example.flavorfinder.view.CameraActivity
import com.example.flavorfinder.view.ui.signin.SigninActivity
import com.example.flavorfinder.view.ui.home.HomeFragment
import com.example.flavorfinder.view.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchBar.inflateMenu(R.menu.search_bar_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_profile -> {
                        val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }

            observeSession()

            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                searchBar.setText(searchView.text)
                searchView.hide()
                val query = searchView.text.toString()
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                performSearch(query)
                false
            }

            setUpCameraButton()
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        navView.setupWithNavController(navController)
    }

    private fun performSearch(query: String) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        if (navHostFragment is NavHostFragment) {
            val homeFragment = navHostFragment.childFragmentManager.fragments[0] as? HomeFragment
            homeFragment?.performSearch(query)
        }
    }

    private fun setUpCameraButton(){
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

    private fun observeSession() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, SigninActivity::class.java))
                finish()
            }
        }
    }
}