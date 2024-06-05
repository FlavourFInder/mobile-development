package com.example.flavorfinder.view.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.pref.UserModel

class MainViewModel(private val repository: MealRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}