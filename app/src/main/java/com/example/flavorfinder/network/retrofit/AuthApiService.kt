package com.example.flavorfinder.network.retrofit

import com.example.flavorfinder.network.response.GetBookmarkResponse
import com.example.flavorfinder.network.response.LoginResponse
import com.example.flavorfinder.network.response.PostBookmarkResponse
import com.example.flavorfinder.network.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("identifier") identifier: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("bookmark")
    suspend fun getBookmark(): GetBookmarkResponse

    @POST("bookmark")
    suspend fun addBookmark(
        @Header("Authorization") token: String,
        @Body recipeId: Map<String, Int>
    ): PostBookmarkResponse

    @DELETE("bookmark/{bookmarkId}")
    suspend fun deleteBookmark(
        @Header("Authorization") token: String,
        @Path("bookmarkId") bookmarkId: String
    ): PostBookmarkResponse

}
