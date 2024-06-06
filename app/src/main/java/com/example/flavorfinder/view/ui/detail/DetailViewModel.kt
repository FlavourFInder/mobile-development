package com.example.flavorfinder.view.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.PostBookmarkResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MealRepository): ViewModel() {

    private val _bookmarkResult = MutableLiveData<Result<PostBookmarkResponse>>()
    val bookmarkResult: LiveData<Result<PostBookmarkResponse>> = _bookmarkResult

    private val _bookmarkStatus = MutableLiveData<Boolean>()
    val bookmarkStatus: LiveData<Boolean> = _bookmarkStatus

    fun addBookmark(recipeId: Int) {
        viewModelScope.launch {
            repository.addBookmark(recipeId).observeForever {
                _bookmarkResult.value = it
            }
        }
    }

//    fun deleteBookmark(bookmarkId: String) {
//        viewModelScope.launch {
//            repository.deleteBookmark(bookmarkId).observeForever {
//                _bookmarkResult.value = it
//            }
//        }
//    }
}