package com.example.flavorfinder.network.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.network.MealPagingSource
import com.example.flavorfinder.network.response.Data
import com.example.flavorfinder.network.response.FilterIngredientResponse
import com.example.flavorfinder.network.response.LoginData
import com.example.flavorfinder.network.response.LoginResponse
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.network.response.MealsResponse
import com.example.flavorfinder.network.response.RegisterResponse
import com.example.flavorfinder.network.retrofit.ApiConfig
import com.example.flavorfinder.network.retrofit.AuthApiService
import com.example.flavorfinder.network.retrofit.MealsApiService
import com.example.flavorfinder.pref.UserModel
import com.example.flavorfinder.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class MealRepository(
    private val mealsApiService: MealsApiService,
    private val authApiService: AuthApiService,
    private val userPreference: UserPreference) {

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = authApiService.register(name, email, password)
            emit(Result.Succes(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(identifier: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = authApiService.login(identifier, password)
            userPreference.saveSession(UserModel(identifier,  response.data.token, true))
//            authApiService = ApiConfig.getAuthApiService(response.token)
            emit(Result.Succes(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

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

    suspend fun getFilterMenu(ingredient: String): FilterIngredientResponse {
        return mealsApiService.filterMeals(ingredient)
    }


    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: MealRepository? = null
        fun getInstance(
            apiService: MealsApiService,
            authApiService: AuthApiService,
            userPreference: UserPreference
        ): MealRepository =
            instance ?: synchronized(this) {
                instance ?: MealRepository(apiService, authApiService, userPreference)
            }.also { instance = it }

    }
}
