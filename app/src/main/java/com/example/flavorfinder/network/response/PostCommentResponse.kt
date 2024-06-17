package com.example.flavorfinder.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PostCommentResponse(

	@field:SerializedName("data")
	val data: CommentData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

@Parcelize
data class CommentData(

	@field:SerializedName("comment_text")
	val commentText: String? = null,

	@field:SerializedName("recipe_id")
	val recipeId: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("comment_id")
	val commentId: String? = null
): Parcelable
