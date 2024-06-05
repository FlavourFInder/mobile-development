package com.example.flavorfinder.network.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class UserRecordResponse(

	@field:SerializedName("emailVerified")
	val emailVerified: Boolean,

	@field:SerializedName("isAnonymous")
	val isAnonymous: Boolean,

	@field:SerializedName("createAt")
	val createAt: String
)

data class Data(

	@field:SerializedName("imgUrl")
	val imgUrl: String,

	@field:SerializedName("email_verified")
	val emailVerified: Boolean,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("userRecordResponse")
	val userRecordResponse: UserRecordResponse,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)
