package com.example.flavorfinder.network.response

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
