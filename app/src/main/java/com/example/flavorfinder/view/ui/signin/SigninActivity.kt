package com.example.flavorfinder.view.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ActivitySigninBinding
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.helper.ViewModelFactory
import com.example.flavorfinder.pref.UserPreference
import com.example.flavorfinder.view.ui.forgotPassword.ForgotPasswordActivity
import com.example.flavorfinder.view.ui.main.MainActivity
import com.example.flavorfinder.view.ui.register.RegisterActivity

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding

    private val viewModel by viewModels<SignInViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signIn_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpTextViewButton()
        setUpButton()
        setUpForgotPasswordButton()
    }

    private fun setUpTextViewButton(){
        binding.GoToRegisterPageTextButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setUpForgotPasswordButton(){
        binding.ForgotPasswordTextButton.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun setUpButton(){
        binding.loginButton.setOnClickListener {
            val identifier = binding.emailOrUsernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            viewModel.login(identifier, password).observe(this) {
                when (it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Error -> {
                        showToast(it.error)
                        showLoading(false)

                    }
                    is Result.Succes -> {
                        showLoading(false)
                        showToast(it.data.message)
                        onLoginSuccess()
                    }
                }
            }

        }
    }

    private fun onLoginSuccess() {
        val sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}