package com.example.flavorfinder.view.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.flavorfinder.databinding.ActivityRegisterBinding
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.helper.ViewModelFactory
import com.example.flavorfinder.view.ui.signin.SigninActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpTextViewButton()
        setUpButton()
    }

    private fun setUpTextViewButton(){
        binding.GoToSignInPageTextButton.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }
    }

    private fun setUpButton(){
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            if (!isFormValid(name, email, password)) {
                showToast("Field tidak boleh kosong")
                return@setOnClickListener
            }

            viewModel.register(name, email, password).observe(this) {
                when (it) {
                    is Result.Loading -> { showToast("Loading") }
                    is Result.Error -> {
                        showToast(it.error)
                    }
                    is Result.Succes -> {
                        startActivity(Intent(this, SigninActivity::class.java))
                        showToast(it.data.message)
                    }
                }
            }
        }
    }

    private fun isFormValid(name: String, email: String, password: String): Boolean {
        val isNameValid = name.isNotEmpty()
        val isEmailValid = email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.isNotEmpty() && password.length >= 8

        return isNameValid && isEmailValid && isPasswordValid
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}