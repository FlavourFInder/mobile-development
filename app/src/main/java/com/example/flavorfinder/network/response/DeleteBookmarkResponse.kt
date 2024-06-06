package com.example.flavorfinder.network.response

import com.google.gson.annotations.SerializedName

data class DeleteBookmarkResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)
