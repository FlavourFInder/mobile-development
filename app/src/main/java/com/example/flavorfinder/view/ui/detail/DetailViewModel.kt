package com.example.flavorfinder.view.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.DataCommentItem
import com.example.flavorfinder.network.response.DataUser
import com.example.flavorfinder.network.response.DeleteBookmarkResponse
import com.example.flavorfinder.network.response.GetCommentResponse
import com.example.flavorfinder.network.response.GetUserProfileResponse
import com.example.flavorfinder.network.response.PostCommentResponse
import com.example.flavorfinder.pref.CommentWithUserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailViewModel(private val repository: MealRepository): ViewModel() {

    private val _bookmarkResult = MutableLiveData<Result<String>>()
    val bookmarkResult: LiveData<Result<String>> = _bookmarkResult

    private val _bookmarkId = MutableLiveData<String?>()
    val bookmarkId: LiveData<String?> = _bookmarkId

    private val _commentResult = MutableLiveData<Result<PostCommentResponse>>()
    val commentResult: LiveData<Result<PostCommentResponse>> = _commentResult

    private val _comments = MutableLiveData<List<DataCommentItem>>()
    val comments: LiveData<List<DataCommentItem>> = _comments

    private val _commentRes = MutableLiveData<Result<List<CommentWithUserProfile>>>()
    val commentRes: LiveData<Result<List<CommentWithUserProfile>>> = _commentRes

    private val _userProfiles = MutableLiveData<Map<String, DataUser>>()
    val userProfiles: LiveData<Map<String, DataUser>> = _userProfiles

    val commentsWithUserProfiles: LiveData<List<CommentWithUserProfile>> = MediatorLiveData<List<CommentWithUserProfile>>().apply {
        addSource(_comments) { comments ->
            val profiles = _userProfiles.value ?: emptyMap()
            value = combineData(comments, profiles)
        }
        addSource(_userProfiles) { profiles ->
            val comments = _comments.value ?: emptyList()
            value = combineData(comments, profiles)
        }
    }

    private val _deleteBookmarkResult = MutableLiveData<Result<DeleteBookmarkResponse>>()
    val deleteBookmarkResult: LiveData<Result<DeleteBookmarkResponse>> = _deleteBookmarkResult

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

    fun addComment(recipeId: String, userId: String, commentText: String) {
        viewModelScope.launch {
            val token = "Bearer ${repository.getSession().first().token}"
            _commentResult.value = Result.Loading
            val result = repository.addComment(token, recipeId, userId, commentText)
            _commentResult.value = result
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

    fun deleteComment(commentId: String, recipeId: String) {
        viewModelScope.launch {
            when (val result = repository.deleteComment(commentId)) {
                is Result.Succes -> getComments(recipeId)
                is Result.Error -> result.error
                is Result.Loading -> {}
            }
        }
    }

    fun getComments(recipeId: String) {
        viewModelScope.launch {
            _commentRes.value = Result.Loading
            try {
                val response = repository.getComments(recipeId)
                if (response is Result.Succes) {
                    val comments = response.data.data
                    _comments.value = comments
                    loadUserProfiles(comments.map { it.userId })
                } else if (response is Result.Error) {
                    _commentRes.value = Result.Error(response.error)
                }
            } catch (e: Exception) {
                _commentResult.value = Result.Error(e.message ?: "Failed to load comments")
            }
        }
    }

    private fun loadUserProfiles(userIds: List<String>) {
        viewModelScope.launch {
            val userProfileMap = mutableMapOf<String, DataUser>()
            for (userId in userIds) {
                val result = repository.getUserById(userId)
                if (result is Result.Succes) {
                    userProfileMap[userId] = result.data.data
                }
            }
            _userProfiles.value = userProfileMap
        }
    }

    private fun combineData(comments: List<DataCommentItem>, profiles: Map<String, DataUser>): List<CommentWithUserProfile> {
        return comments.mapNotNull { comment ->
            val userProfile = profiles[comment.userId]
            if (userProfile != null) {
                CommentWithUserProfile(comment, userProfile)
            } else {
                null
            }
        }.asReversed()
    }
}