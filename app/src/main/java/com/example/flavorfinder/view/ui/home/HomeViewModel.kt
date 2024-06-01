package com.example.flavorfinder.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.MealsItem

class HomeViewModel(mealRepository: MealRepository): ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val meal: LiveData<PagingData<MealsItem>> =
        mealRepository.getMeals().cachedIn(viewModelScope)
}