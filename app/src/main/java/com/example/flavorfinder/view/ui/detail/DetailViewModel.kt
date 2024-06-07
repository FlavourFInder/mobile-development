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

    private val _bookmarkResult = MutableLiveData<Result<String>>()
    val bookmarkResult: LiveData<Result<String>> = _bookmarkResult

    private val _bookmarkId = MutableLiveData<String?>()
    val bookmarkId: LiveData<String?> = _bookmarkId

    private val _deleteBookmarkResult = MutableLiveData<Result<DeleteBookmarkResponse>>()
    val deleteBookmarkResult: LiveData<Result<DeleteBookmarkResponse>> = _deleteBookmarkResult

    private val _bookmarkStatus = MutableLiveData<Boolean>()
    val bookmarkStatus: LiveData<Boolean> = _bookmarkStatus

    fun checkIfBookmarked(recipeId: Int): LiveData<Result<String?>> {
        val result = MutableLiveData<Result<String?>>()
        viewModelScope.launch {
            try {
                val bookmarks = repository.getBookmarks()
                val bookmark = bookmarks.data.firstOrNull { it.recipe.idMeal?.toInt() == recipeId }
                result.postValue(Result.Succes(bookmark?.bookmarkId))
            } catch (e: Exception) {
                result.postValue(Result.Error(e.message ?: "Unknown error"))
            }
        }
        return result
    }

    fun addBookmark(recipeId: Int) {
        viewModelScope.launch {
            _bookmarkResult.value = Result.Loading
            try {
                val response = repository.addBookmark(recipeId)
                _bookmarkId.value = response.data.bookmarkId
                _bookmarkResult.value = Result.Succes("Bookmark added!")
            } catch (e: Exception) {
                _bookmarkResult.value = Result.Error(e.message ?: "An error occured")
            }

        }
    }

    fun deleteBookmark(bookmarkId: String) {
        viewModelScope.launch {
            _bookmarkResult.value = Result.Loading
            try {
                repository.deleteBookmark(bookmarkId)
                _bookmarkId.value = null
                _bookmarkResult.value = Result.Succes("Bookmark deleted!")
            } catch (e: Exception) {
                _deleteBookmarkResult.value = Result.Error(e.message ?: "An error occured")
            }
        }
    }
}