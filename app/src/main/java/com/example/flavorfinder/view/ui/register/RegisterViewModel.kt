package com.example.flavorfinder.view.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.RegisterResponse

class RegisterViewModel(private val authRepository: MealRepository) : ViewModel() {

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> {
        return authRepository.register(name, email, password)
    }
}