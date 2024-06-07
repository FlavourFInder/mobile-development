package com.example.flavorfinder.network.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.network.MealPagingSource
import com.example.flavorfinder.network.response.DeleteBookmarkResponse
import com.example.flavorfinder.network.response.FilterIngredientResponse
import com.example.flavorfinder.network.response.GetBookmarkResponse
import com.example.flavorfinder.network.response.LoginResponse
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.network.response.MealsResponse
import com.example.flavorfinder.network.response.PostBookmarkResponse
import com.example.flavorfinder.network.response.RegisterResponse
import com.example.flavorfinder.network.retrofit.AuthApiService
import com.example.flavorfinder.network.retrofit.MealsApiService
import com.example.flavorfinder.pref.UserModel
import com.example.flavorfinder.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class MealRepository(
    private val mealsApiService: MealsApiService,
    private val authApiService: AuthApiService,
    private val userPreference: UserPreference
) {

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
            userPreference.saveSession(UserModel(identifier, response.data.token, true))
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

//    fun addBookmark(recipeId: Int): LiveData<Result<PostBookmarkResponse>> = liveData {
//        emit(Result.Loading)
//        try {
//            val token = userPreference.getSession().first().token
//            val response = authApiService.addBookmark("Bearer $token", mapOf("recipeId" to recipeId))
//            emit(Result.Succes(response))
//        } catch (e: Exception) {
//            emit(Result.Error(e.message.toString()))
//        }
//    }

    suspend fun addBookmark(recipeId: Int): PostBookmarkResponse {
        val token = userPreference.getSession().first().token
        return authApiService.addBookmark("Bearer $token", mapOf("recipeId" to recipeId))
    }

    suspend fun lookupMeals(mealId: String): MealsResponse {
        return mealsApiService.lookupMeals(mealId)
    }

    suspend fun getBookmark(): Result<GetBookmarkResponse> {
        return try {
            val token = userPreference.getSession().first().token
            val response = authApiService.getBookmark("Bearer $token")
            Result.Succes(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: "An error occured")
        }
    }

    suspend fun getBookmarks(): GetBookmarkResponse {
        val token = userPreference.getSession().first().token
        return authApiService.getBookmark("Bearer $token")
    }

    suspend fun deleteBookmark(bookmarkId: String){
        val token = getSession().first().token
        authApiService.deleteBookmark("Bearer $token", bookmarkId)
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
