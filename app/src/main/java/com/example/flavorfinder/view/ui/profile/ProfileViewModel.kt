package com.example.flavorfinder.view.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flavorfinder.network.repository.MealRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: MealRepository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getUserData() {
        //belum ku kerja
    }
}