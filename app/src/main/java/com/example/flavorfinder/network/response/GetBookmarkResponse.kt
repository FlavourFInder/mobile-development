package com.example.flavorfinder.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetBookmarkResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataItem(

	@field:SerializedName("bookmark_id")
	val bookmarkId: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("recipe")
	val recipe: GetBookmarkRecipe,

	@field:SerializedName("time")
	val time: String
)

@Parcelize
data class GetBookmarkRecipe(

	@field:SerializedName("strImageSource")
	val strImageSource: String? = "",

	@field:SerializedName("strIngredient10")
	val strIngredient10: String? = "",

	@field:SerializedName("strIngredient12")
	val strIngredient12: String? = "",

	@field:SerializedName("strIngredient11")
	val strIngredient11: String? = "",

	@field:SerializedName("strIngredient14")
	val strIngredient14: String? = "",

	@field:SerializedName("strCategory")
	val strCategory: String? = "",

	@field:SerializedName("strIngredient13")
	val strIngredient13: String? = "",

	@field:SerializedName("strIngredient16")
	val strIngredient16: String? = "",

	@field:SerializedName("strIngredient15")
	val strIngredient15: String? = "",

	@field:SerializedName("strIngredient18")
	val strIngredient18: String? = "",

	@field:SerializedName("strIngredient17")
	val strIngredient17: String? = "",

	@field:SerializedName("strArea")
	val strArea: String? = "",

	@field:SerializedName("strCreativeCommonsConfirmed")
	val strCreativeCommonsConfirmed: String? = "",

	@field:SerializedName("strIngredient19")
	val strIngredient19: String? = "",

	@field:SerializedName("strTags")
	val strTags: String? = "",

	@field:SerializedName("idMeal")
	val idMeal: String? = "",

	@field:SerializedName("strInstructions")
	val strInstructions: String? = "",

	@field:SerializedName("strIngredient1")
	val strIngredient1: String? = "",

	@field:SerializedName("strIngredient3")
	val strIngredient3: String? = "",

	@field:SerializedName("strIngredient2")
	val strIngredient2: String? = "",

	@field:SerializedName("strIngredient20")
	val strIngredient20: String? = "",

	@field:SerializedName("strIngredient5")
	val strIngredient5: String? = "",

	@field:SerializedName("strIngredient4")
	val strIngredient4: String? = "",

	@field:SerializedName("strIngredient7")
	val strIngredient7: String? = "",

	@field:SerializedName("strIngredient6")
	val strIngredient6: String? = "",

	@field:SerializedName("strIngredient9")
	val strIngredient9: String? = "",

	@field:SerializedName("strIngredient8")
	val strIngredient8: String? = "",

	@field:SerializedName("strMealThumb")
	val strMealThumb: String? = "",

	@field:SerializedName("strMeasure20")
	val strMeasure20: String? = "",

	@field:SerializedName("strYoutube")
	val strYoutube: String? = "",

	@field:SerializedName("strMeal")
	val strMeal: String? = "",

	@field:SerializedName("strMeasure12")
	val strMeasure12: String? = "",

	@field:SerializedName("strMeasure13")
	val strMeasure13: String? = "",

	@field:SerializedName("strMeasure10")
	val strMeasure10: String? = "",

	@field:SerializedName("strMeasure11")
	val strMeasure11: String? = "",

	@field:SerializedName("dateModified")
	val dateModified: String? = "",

	@field:SerializedName("strDrinkAlternate")
	val strDrinkAlternate: String? = "",

	@field:SerializedName("strSource")
	val strSource: String? = "",

	@field:SerializedName("strMeasure9")
	val strMeasure9: String? = "",

	@field:SerializedName("strMeasure7")
	val strMeasure7: String? = "",

	@field:SerializedName("strMeasure8")
	val strMeasure8: String? = "",

	@field:SerializedName("strMeasure5")
	val strMeasure5: String? = "",

	@field:SerializedName("strMeasure6")
	val strMeasure6: String? = "",

	@field:SerializedName("strMeasure3")
	val strMeasure3: String? = "",

	@field:SerializedName("strMeasure4")
	val strMeasure4: String? = "",

	@field:SerializedName("strMeasure1")
	val strMeasure1: String? = "",

	@field:SerializedName("strMeasure18")
	val strMeasure18: String? = "",

	@field:SerializedName("strMeasure2")
	val strMeasure2: String? = "",

	@field:SerializedName("strMeasure19")
	val strMeasure19: String? = "",

	@field:SerializedName("strMeasure16")
	val strMeasure16: String? = "",

	@field:SerializedName("strMeasure17")
	val strMeasure17: String? = "",

	@field:SerializedName("strMeasure14")
	val strMeasure14: String? = "",

	@field:SerializedName("strMeasure15")
	val strMeasure15: String? = ""
): Parcelable
