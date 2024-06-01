package com.example.flavorfinder.di

import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.retrofit.ApiConfig
import com.example.flavorfinder.view.ui.home.HomeFragment

object Injection {
    fun provideRepository(context: HomeFragment): MealRepository {
        val apiService = ApiConfig.getMealsApiService()
        return MealRepository.getInstance(apiService)
    }
}