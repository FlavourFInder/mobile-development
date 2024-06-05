package com.example.flavorfinder.network.response

import com.google.gson.annotations.SerializedName

data class FilterIngredientResponse(

	@field:SerializedName("meals")
	val meals: List<MealsItem?>? = null
)

data class FilterItem(

	@field:SerializedName("strMealThumb")
	val strMealThumb: String? = null,

	@field:SerializedName("idMeal")
	val idMeal: String? = null,

	@field:SerializedName("strMeal")
	val strMeal: String? = null
)
