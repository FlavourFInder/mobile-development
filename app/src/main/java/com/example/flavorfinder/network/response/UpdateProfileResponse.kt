package com.example.flavorfinder.network.response

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(

    @field:SerializedName("data")
    val data: DataUserUpdate,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Int
)

data class DataUserUpdate(

    @field:SerializedName("imgUrl")
    val imgUrl: String,

    @field:SerializedName("email_verified")
    val emailVerified: Boolean,

    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String
)
