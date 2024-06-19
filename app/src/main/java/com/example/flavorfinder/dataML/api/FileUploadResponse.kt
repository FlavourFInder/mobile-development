package com.example.flavorfinder.dataML.api

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

	@field:SerializedName("percentage")
	val percentage: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
