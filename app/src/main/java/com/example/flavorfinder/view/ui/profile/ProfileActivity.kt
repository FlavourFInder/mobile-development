package com.example.flavorfinder.view.ui.profile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ActivityProfileBinding
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.helper.ViewModelFactory
import com.example.flavorfinder.network.response.Data
import com.example.flavorfinder.network.response.LoginResponse
import com.example.flavorfinder.pref.UserModel
import com.example.flavorfinder.pref.UserPreference
import com.example.flavorfinder.pref.dataStore
import com.example.flavorfinder.view.ui.signin.SigninActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityProfileBinding

    private lateinit var userPreference: UserPreference

    private lateinit var btnConfirmLogout: Button

    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        lifecycleScope.launch {
            viewModel.getUserData()
        }

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.user.observe(this) { result ->
            when (result) {
                is Result.Succes -> {
                    val userData = result.data.data
                    binding.apply {
                        tvEmail.text = userData.email
                        tvUsername.text = userData.username
                        Glide.with(this@ProfileActivity)
                            .load(userData.imgUrl)
                            .placeholder(R.drawable.baseline_account_circle_24)
                            .into(ivProfile)
                    }
                }
                is Result.Error -> {
                    showToast(result.error)
                }
                is Result.Loading -> { showToast("Loading...") }
            }
        }
    }

    private fun showLogoutDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog_logout)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg))
        dialog.setCancelable(false)

        btnCancel = dialog.findViewById(R.id.btn_cancel)
        btnConfirmLogout = dialog.findViewById(R.id.btn_dialog_logout)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirmLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}