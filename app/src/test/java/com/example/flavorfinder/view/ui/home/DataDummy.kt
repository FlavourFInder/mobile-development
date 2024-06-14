package com.example.flavorfinder.view.ui.home

import com.example.flavorfinder.network.response.MealsItem

object DataDummy {
    fun generateDummyMealsItem(): List<MealsItem> {
        val mealsList = ArrayList<MealsItem>()
        for (i in 0..10) {
            val meals = MealsItem(
                strImageSource = "https://example.com/image$i.png",
                strIngredient10 = "Ingredient10_$i",
                strIngredient12 = "Ingredient12_$i",
                strIngredient11 = "Ingredient11_$i",
                strIngredient14 = "Ingredient14_$i",
                strCategory = "Category_$i",
                strIngredient13 = "Ingredient13_$i",
                strIngredient16 = "Ingredient16_$i",
                strIngredient15 = "Ingredient15_$i",
                strIngredient18 = "Ingredient18_$i",
                strIngredient17 = "Ingredient17_$i",
                strArea = "Area_$i",
                strCreativeCommonsConfirmed = "Yes",
                strIngredient19 = "Ingredient19_$i",
                strTags = "Tags_$i",
                idMeal = "idMeal_$i",
                strInstructions = "Instructions_$i",
                strIngredient1 = "Ingredient1_$i",
                strIngredient3 = "Ingredient3_$i",
                strIngredient2 = "Ingredient2_$i",
                strIngredient20 = "Ingredient20_$i",
                strIngredient5 = "Ingredient5_$i",
                strIngredient4 = "Ingredient4_$i",
                strIngredient7 = "Ingredient7_$i",
                strIngredient6 = "Ingredient6_$i",
                strIngredient9 = "Ingredient9_$i",
                strIngredient8 = "Ingredient8_$i",
                strMealThumb = "https://example.com/thumb$i.png",
                strMeasure20 = "Measure20_$i",
                strYoutube = "https://youtube.com/example$i",
                strMeal = "Meal_$i",
                strMeasure12 = "Measure12_$i",
                strMeasure13 = "Measure13_$i",
                strMeasure10 = "Measure10_$i",
                strMeasure11 = "Measure11_$i",
                dateModified = "2022-02-22",
                strDrinkAlternate = "DrinkAlternate_$i",
                strSource = "https://source.com/$i",
                strMeasure9 = "Measure9_$i",
                strMeasure7 = "Measure7_$i",
                strMeasure8 = "Measure8_$i",
                strMeasure5 = "Measure5_$i",
                strMeasure6 = "Measure6_$i",
                strMeasure3 = "Measure3_$i",
                strMeasure4 = "Measure4_$i",
                strMeasure1 = "Measure1_$i",
                strMeasure18 = "Measure18_$i",
                strMeasure2 = "Measure2_$i",
                strMeasure19 = "Measure19_$i",
                strMeasure16 = "Measure16_$i",
                strMeasure17 = "Measure17_$i",
                strMeasure14 = "Measure14_$i",
                strMeasure15 = "Measure15_$i"
            )
            mealsList.add(meals)
        }
        return mealsList
    }
}
