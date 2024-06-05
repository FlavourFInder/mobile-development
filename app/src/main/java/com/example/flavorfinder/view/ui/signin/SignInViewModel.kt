package com.example.flavorfinder.view.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.LoginResponse

class SignInViewModel(private val repository: MealRepository) : ViewModel() {
    fun login(identifier: String, password: String): LiveData<Result<LoginResponse>> {
        return repository.login(identifier, password)
    }
}