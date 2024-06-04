package com.example.flavorfinder.dataML.api

import retrofit2.http.Body
import retrofit2.http.POST

interface MLApiService {
    @POST("classify")
    suspend fun uploadImage(
        @Body payload: ImagePayload
    ): FileUploadResponse
}

data class ImagePayload(
    val payload: String
)
