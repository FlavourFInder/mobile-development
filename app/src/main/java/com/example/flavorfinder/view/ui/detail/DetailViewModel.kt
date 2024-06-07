package com.example.flavorfinder.view.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.DeleteBookmarkResponse
import com.example.flavorfinder.network.response.PostBookmarkResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MealRepository): ViewModel() {

    private val _bookmarkResult = MutableLiveData<Result<PostBookmarkResponse>>()
    val bookmarkResult: LiveData<Result<PostBookmarkResponse>> = _bookmarkResult

    private val _deleteBookmarkResult = MutableLiveData<Result<DeleteBookmarkResponse>>()
    val deleteBookmarkResult: LiveData<Result<DeleteBookmarkResponse>> = _deleteBookmarkResult

    private val _bookmarkStatus = MutableLiveData<Boolean>()
    val bookmarkStatus: LiveData<Boolean> = _bookmarkStatus

    fun checkBookmarkStatus(recipeId: Int) {
        viewModelScope.launch {
            try {
                val isBookmarked = repository.isBookmarked(recipeId)
                _bookmarkStatus.value = isBookmarked
            } catch (e: Exception) {
                _bookmarkStatus.value = false
            }
        }
    }

    fun addBookmark(recipeId: Int) {
        viewModelScope.launch {
            repository.addBookmark(recipeId).observeForever {
                _bookmarkResult.value = it
            }
        }
    }

    fun deleteBookmark(bookmarkId: String) {
        viewModelScope.launch {
            try {
                val result = repository.deleteBookmark(bookmarkId)
                _deleteBookmarkResult.value = result
            } catch (e: Exception) {
                _deleteBookmarkResult.value = Result.Error(e.message ?: "An error occured")
            }
        }
    }
}