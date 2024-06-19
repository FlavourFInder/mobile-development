package com.example.flavorfinder.view.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.GetUserProfileResponse
import com.example.flavorfinder.pref.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(private val repository: MealRepository) : ViewModel() {

    private val _user = MutableLiveData<Result<GetUserProfileResponse>>()
    val user: LiveData<Result<GetUserProfileResponse>> = _user

    private val _userDataResult = MutableLiveData<Result<String>>()
    val userDataResult: LiveData<Result<String>> = _userDataResult

    private lateinit var userPreference: UserPreference

    fun getUserData() {
        viewModelScope.launch {
            _user.value = Result.Loading
            val result = repository.getUser()
            _user.value = result
        }
    }

    fun updateUsername(username: String): LiveData<Result<GetUserProfileResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        val result = repository.updateUsername(username)
        emit(result)
    }

    fun updateProfilePicture(image: File): LiveData<Result<GetUserProfileResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        val result = repository.updateProfilePicture(image)
        emit(result)
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}