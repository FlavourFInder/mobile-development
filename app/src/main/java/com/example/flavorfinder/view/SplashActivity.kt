package com.example.flavorfinder.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ActivitySplashBinding
import com.example.flavorfinder.pref.UserPreference
import com.example.flavorfinder.pref.dataStore
import com.example.flavorfinder.view.ui.main.MainActivity
import com.example.flavorfinder.view.ui.signin.SigninActivity
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var userPreference: UserPreference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userPreference = UserPreference.getInstance(dataStore)

        playAnimation(binding.ivSplash)
        playAnimation(binding.tvLogo)

        lifecycleScope.launch {
            if (isTokenExpired()) {
                val intent = Intent(this@SplashActivity, SigninActivity::class.java)
                startActivity(intent)
                finish()
            }  else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    private suspend fun isTokenExpired(): Boolean {
        val userPreference = UserPreference.getInstance(applicationContext.dataStore)
        return userPreference.isTokenExpired()
    }

    private fun playAnimation(view: View) {
        ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
            duration = 1500
            start()
        }
    }

}