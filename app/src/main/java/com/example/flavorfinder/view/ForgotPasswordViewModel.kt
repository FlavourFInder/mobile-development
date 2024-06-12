package com.example.flavorfinder.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.ForgotPasswordResponse

class ForgotPasswordViewModel(private val repository: MealRepository) : ViewModel() {
    fun forgotPassword(email:String): LiveData<Result<ForgotPasswordResponse>> {
        return repository.forgotPassword(email)
    }
}