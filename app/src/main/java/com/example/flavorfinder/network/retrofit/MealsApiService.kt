package com.example.flavorfinder.network.retrofit

import com.example.flavorfinder.network.response.FilterIngredientResponse
import com.example.flavorfinder.network.response.MealsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealsApiService {

    @GET("random.php")
    suspend fun getMeals(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): MealsResponse

    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") query: String
    ): MealsResponse

    @GET("filter.php")
    suspend fun filterMeals(
        @Query("i") ingredient: String
    ): FilterIngredientResponse

    @GET("lookup.php")
    suspend fun lookupMeals(
        @Query("i") query: String
    ): MealsResponse
}
