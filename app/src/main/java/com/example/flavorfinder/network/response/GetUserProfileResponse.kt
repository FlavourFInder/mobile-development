package com.example.flavorfinder.network.response

import com.google.gson.annotations.SerializedName

data class GetUserProfileResponse(

	@field:SerializedName("data")
	val data: DataUser,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataUser(

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
