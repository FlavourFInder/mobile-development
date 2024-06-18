package com.example.flavorfinder.view.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.GetUserProfileResponse
import com.example.flavorfinder.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MealRepository) : ViewModel() {
    private val _user = MutableLiveData<Result<GetUserProfileResponse>>()
    val user: LiveData<Result<GetUserProfileResponse>> = _user

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getUserImageProfile() {
        viewModelScope.launch {
            val result = repository.getUser()
            _user.value = result
        }
    }
}