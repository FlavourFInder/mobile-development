package com.example.flavorfinder.pref

data class UserModel(
    val email: String,
    val userId: String,
    val token: String,
    val isLogin: Boolean = false
)