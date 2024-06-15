package com.example.flavorfinder.view.ui.forgotPassword

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ActivityForgotPasswordBinding
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.helper.ViewModelFactory

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel by viewModels<ForgotPasswordViewModel>{
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgotPassword_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpButton()
    }

    private fun setUpButton() {
        binding.resetPasswordButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            Log.d("ForgotPasswordActivity", "Button clicked with email: $email")

            if (email.isNotEmpty()) {
                viewModel.forgotPassword(email).observe(this) {
                    when (it) {
                        is Result.Loading -> {
                            showLoading()
                        }
                        is Result.Error -> {
                            hideLoading()
                            showToast(it.error)
                        }
                        is Result.Succes -> {
                            hideLoading()
                            it.data.message?.let { message -> showToast(message) }
                        }
                    }
                }
            } else {
                showToast("Please enter an email")
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}