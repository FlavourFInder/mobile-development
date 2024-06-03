package com.example.flavorfinder.network.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flavorfinder.network.MealPagingSource
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.network.response.MealsResponse
import com.example.flavorfinder.network.retrofit.MealsApiService

class MealRepository(private val mealsApiService: MealsApiService) {

    fun getMeals(): LiveData<PagingData<MealsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = {
                MealPagingSource(mealsApiService)
            }
        ).liveData
    }

    suspend fun searchMeals(query: String): MealsResponse {
        return mealsApiService.searchMeals(query)
    }

    companion object {
        @Volatile
        private var instance: MealRepository? = null
        fun getInstance(apiService: MealsApiService): MealRepository =
            instance ?: synchronized(this) {
                instance ?: MealRepository(apiService)
            }.also { instance = it }
    }
}
