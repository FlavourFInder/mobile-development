package com.example.flavorfinder.view.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.DataItem
import com.example.flavorfinder.network.response.GetBookmarkResponse
import com.example.flavorfinder.network.response.MealsItem
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: MealRepository) : ViewModel() {

    private val _bookmarks = MutableLiveData<Result<GetBookmarkResponse>>()
    val bookmarks: LiveData<com.example.flavorfinder.helper.Result<GetBookmarkResponse>> = _bookmarks
    fun getBookmarks() {
        viewModelScope.launch {
            val result = repository.getBookmark()
            _bookmarks.value = result
        }
    }
}