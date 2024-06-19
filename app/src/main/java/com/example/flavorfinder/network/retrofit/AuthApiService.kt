package com.example.flavorfinder.network.retrofit

import com.example.flavorfinder.network.response.DeleteBookmarkResponse
import com.example.flavorfinder.network.response.DeleteCommentResponse
import com.example.flavorfinder.network.response.ForgotPasswordResponse
import com.example.flavorfinder.network.response.GetBookmarkResponse
import com.example.flavorfinder.network.response.GetCommentResponse
import com.example.flavorfinder.network.response.GetUserProfileResponse
import com.example.flavorfinder.network.response.LoginResponse
import com.example.flavorfinder.network.response.PostBookmarkResponse
import com.example.flavorfinder.network.response.PostCommentResponse
import com.example.flavorfinder.network.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @FormUrlEncoded
    @POST("password-reset")
    suspend fun forgotPassword(
        @Field("email") email: String
    ): ForgotPasswordResponse

    @GET("bookmark")
    suspend fun getBookmark(
        @Header("Authorization") token: String
    ): GetBookmarkResponse

    @POST("bookmark")
    suspend fun addBookmark(
        @Header("Authorization") token: String,
        @Body recipeId: Map<String, Int>
    ): PostBookmarkResponse

    @DELETE("bookmark/{bookmarkId}")
    suspend fun deleteBookmark(
        @Header("Authorization") token: String,
        @Path("bookmarkId") bookmarkId: String
    ): DeleteBookmarkResponse

    @GET("user/{user_id}")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("user_id") userId: String
    ): GetUserProfileResponse

    @Multipart
    @PUT("user/{user_id}")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Path("user_id") userId: String,
        @Part profileImage: MultipartBody.Part,
        @Part("username") username: RequestBody
    ): GetUserProfileResponse

    @FormUrlEncoded
    @POST("{recipe_id}/comments")
    suspend fun addComment(
        @Header("Authorization") token: String,
        @Path("recipe_id") recipeId: String,
        @Field("user_id") userId: String,
        @Field("comment_text") commentText: String
    ): PostCommentResponse

    @GET("{recipe_id}/comments")
    suspend fun getComments(
        @Header("Authorization") token: String,
        @Path("recipe_id") recipeId: String
    ): GetCommentResponse

    @DELETE("comments/{comment_id}")
    suspend fun deleteComment(
        @Header("Authorization") token: String,
        @Path("comment_id") commentId: String
    ): DeleteCommentResponse

}
