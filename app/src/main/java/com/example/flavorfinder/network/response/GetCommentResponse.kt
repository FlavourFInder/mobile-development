package com.example.flavorfinder.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetCommentResponse(

	@field:SerializedName("data")
	val data: List<DataCommentItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

@Parcelize
data class DataCommentItem(

	@field:SerializedName("comment_text")
	val commentText: String,

	@field:SerializedName("recipe_id")
	val recipeId: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("comment_id")
	val commentId: String
): Parcelable
