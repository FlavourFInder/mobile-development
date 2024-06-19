package com.example.flavorfinder.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class FilterIngredientResponse(

	@field:SerializedName("meals")
	val meals: List<MealsItem?>? = null
)

@Parcelize
data class FilterItem(

	@field:SerializedName("strMealThumb")
	val strMealThumb: String? = null,

	@field:SerializedName("idMeal")
	val idMeal: String? = null,

	@field:SerializedName("strMeal")
	val strMeal: String? = null
) : Parcelable
