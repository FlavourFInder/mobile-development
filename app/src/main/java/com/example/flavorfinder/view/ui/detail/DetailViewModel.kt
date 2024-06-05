package com.example.flavorfinder.view.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailViewModel: ViewModel() {
    private val _isBookmark = MutableLiveData<Boolean>()
    val isBookmark: LiveData<Boolean> = _isBookmark
}