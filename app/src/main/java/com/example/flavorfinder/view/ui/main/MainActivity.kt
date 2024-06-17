package com.example.flavorfinder.view.ui.main

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ActivityMainBinding
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.helper.ViewModelFactory
import com.example.flavorfinder.network.response.GetUserProfileResponse
import com.example.flavorfinder.pref.UserPreference
import com.example.flavorfinder.pref.dataStore
import com.example.flavorfinder.view.ui.filterAndCamera.CameraActivity
import com.example.flavorfinder.view.ui.home.HomeFragment
import com.example.flavorfinder.view.ui.profile.ProfileActivity
import com.example.flavorfinder.view.ui.signin.SigninActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var userPreference: UserPreference

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

            setupProfileIcon()
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

        viewModel.user.observeForever{ result ->
            when (result) {
                is Result.Succes -> {
                    setupProfileIcon(result.data)
                }
                is Result.Error -> {
                    handleError(result.error)
                }
                is Result.Loading -> {
                    showLoading(true)
                }
            }
        }
        viewModel.getUserImageProfile()
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

    private fun setupProfileIcon(userProfile: GetUserProfileResponse? = null) {
        val profileMenuItem = binding.searchBar.menu.findItem(R.id.action_profile)
        val profileView = layoutInflater.inflate(R.layout.menu_item_profile, null)
        val profileImageView = profileView.findViewById<CircleImageView>(R.id.iv_profile)

        val userProfileImageUrl = userProfile?.data?.imgUrl ?: "DEFAULT_IMAGE_URL"
        Glide.with(this)
            .load(userProfileImageUrl)
            .placeholder(R.drawable.baseline_account_circle_24)
            .into(profileImageView)

        profileMenuItem.actionView = profileView
        profileView.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
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

    private fun handleError(errorMessage: String) {
        when {
            errorMessage.contains("401") -> {
                showSessionExpiredDialog()
            }
            errorMessage.contains("403") -> {
                showSessionExpiredDialog()
            }
            else -> {
                showToast(errorMessage)
            }
        }
    }

    private fun showSessionExpiredDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog_session_expired)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.custom_dialog_bg))
        dialog.setCancelable(false)

        val btnDialogSessionExpired: Button = dialog.findViewById(R.id.btn_dialog_session_expired)
        btnDialogSessionExpired.setOnClickListener {
            dialog.dismiss()
            lifecycleScope.launch {
                userPreference = UserPreference.getInstance(dataStore)
                userPreference.logout()
                val intent = Intent(this@MainActivity, SigninActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        dialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserImageProfile()
    }

}