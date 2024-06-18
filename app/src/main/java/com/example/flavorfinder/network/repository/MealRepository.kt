package com.example.flavorfinder.network.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.helper.reduceFileImage
import com.example.flavorfinder.network.MealPagingSource
import com.example.flavorfinder.network.response.DeleteCommentResponse
import com.example.flavorfinder.network.response.FilterIngredientResponse
import com.example.flavorfinder.network.response.ForgotPasswordResponse
import com.example.flavorfinder.network.response.GetBookmarkResponse
import com.example.flavorfinder.network.response.GetCommentResponse
import com.example.flavorfinder.network.response.GetUserProfileResponse
import com.example.flavorfinder.network.response.LoginResponse
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.network.response.MealsResponse
import com.example.flavorfinder.network.response.PostBookmarkResponse
import com.example.flavorfinder.network.response.PostCommentResponse
import com.example.flavorfinder.network.response.RegisterResponse
import com.example.flavorfinder.network.retrofit.AuthApiService
import com.example.flavorfinder.network.retrofit.MealsApiService
import com.example.flavorfinder.pref.UserModel
import com.example.flavorfinder.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

open class MealRepository(
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
            userPreference.saveSession(UserModel(identifier, response.data.user.userId, response.data.token, true))
            emit(Result.Succes(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun forgotPassword(email: String): LiveData<Result<ForgotPasswordResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = authApiService.forgotPassword(email)
            Log.d("API Response", response.toString())
            emit(Result.Succes(response))
        } catch (e: Exception) {
            Log.e("API Error", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    open fun getMeals(): LiveData<PagingData<MealsItem>> {
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

    suspend fun addBookmark(recipeId: Int): PostBookmarkResponse {
        val token = userPreference.getSession().first().token
        return authApiService.addBookmark("Bearer $token", mapOf("recipeId" to recipeId))
    }

    suspend fun addComment(token: String, recipeId: String, userId: String, commentText: String): Result<PostCommentResponse> {
        return try {
            val response = authApiService.addComment(
                token = token,
                recipeId = recipeId,
                userId = userId,
                commentText = commentText
            )
            Result.Succes(response)
        } catch (e: HttpException) {
            Result.Error(e.localizedMessage ?: "An error occurred")
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "An error occurred")
        }
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
            Result.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun getComments(recipeId: String): Result<GetCommentResponse> {
        return try {
            val token = userPreference.getSession().first().token
            val response = authApiService.getComments("Bearer $token", recipeId)
            Result.Succes(response)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    suspend fun getBookmarks(): GetBookmarkResponse {
        val token = userPreference.getSession().first().token
        return authApiService.getBookmark("Bearer $token")
    }

    suspend fun deleteBookmark(bookmarkId: String) {
        val token = getSession().first().token
        authApiService.deleteBookmark("Bearer $token", bookmarkId)
    }

    suspend fun deleteComment(commentId: String): Result<DeleteCommentResponse> {
        return try {
            val token = getSession().first().token
            val response = authApiService.deleteComment("Bearer $token", commentId)
            if (response.status == 200) {
                Result.Succes(response)
            } else {
                Result.Error("Failed to delete comment")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "An error occured")
        }
    }

    suspend fun getUser(): Result<GetUserProfileResponse> {
        return try {
            val token = userPreference.getSession().first().token
            val userId = userPreference.getSession().first().userId

            run {
                val response = authApiService.getUser("Bearer $token", userId)
                Result.Succes(response)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "An error occured")
        }
    }

    suspend fun getUserById(userId: String): Result<GetUserProfileResponse> {
        return try {
            val token = userPreference.getSession().first().token
            val response = authApiService.getUser("Bearer $token", userId)
            Result.Succes(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun updateUsername(username: String): Result<GetUserProfileResponse> {
        return try {
            val token = userPreference.getUser().token
            val userId = userPreference.getUser().userId

            val usernameRequestBody = RequestBody.create("text/plain".toMediaType(), username)
            val emptyFile = MultipartBody.Part.createFormData("file", "")

            val response = authApiService.updateUserProfile(
                "Bearer $token",
                userId = userId,
                profileImage = emptyFile,
                username = usernameRequestBody
            )
            Result.Succes(response)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "An error occured")
        }
    }

    suspend fun updateProfilePicture(image: File?): Result<GetUserProfileResponse> {
        return try {
            val token = userPreference.getUser().token
            val userId = userPreference.getUser().userId

            val emptyUsername = "".toRequestBody("text/plain".toMediaTypeOrNull())
            val imageFile = reduceFileImage(image!!)
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "profileImage",
                imageFile.name,
                requestImageFile
            )
            val response = authApiService.updateUserProfile(
                "Bearer $token",
                userId = userId,
                profileImage = multipartBody,
                username = emptyUsername
            )
            Result.Succes(response)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "An error occured")
        }
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