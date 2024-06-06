package com.example.flavorfinder.di

import android.content.Context
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.retrofit.ApiConfig
import com.example.flavorfinder.pref.UserPreference
import com.example.flavorfinder.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): MealRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val authApiService = ApiConfig.getAuthApiService()
        val mealsApiService = user?.token?.let {
            ApiConfig.getMealsApiService(it)
        } ?: throw IllegalStateException("User token not found")
        return MealRepository.getInstance(mealsApiService, authApiService, pref)
    }
}