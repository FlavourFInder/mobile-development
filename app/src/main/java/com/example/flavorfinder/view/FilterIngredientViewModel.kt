package com.example.flavorfinder.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.FilterItem
import com.example.flavorfinder.network.response.MealsItem
import kotlinx.coroutines.launch

class FilterIngredientViewModel(private val mealRepository: MealRepository) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is filter result activity"
    }
    val text: LiveData<String> = _text

    private val _filterResult = MutableLiveData<List<FilterItem>?>()
    val filterResult: LiveData<List<FilterItem>?> = _filterResult

    fun filterMeals(ingredient: String) {
        viewModelScope.launch {
            try {
                val response = mealRepository.getFilterMenu(ingredient)
                val filterItems = response.meals?.mapNotNull { it?.toFilterItem() }
                _filterResult.postValue(filterItems)
            } catch (e: Exception) {
                _filterResult.postValue(null)
            }
        }
    }

    private fun MealsItem.toFilterItem(): FilterItem {
        return FilterItem(
            strMealThumb = this.strMealThumb,
            idMeal = this.idMeal,
            strMeal = this.strMeal
        )
    }
}
