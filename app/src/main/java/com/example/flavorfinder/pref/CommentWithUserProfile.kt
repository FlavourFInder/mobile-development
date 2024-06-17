package com.example.flavorfinder.pref

import com.example.flavorfinder.network.response.DataCommentItem
import com.example.flavorfinder.network.response.DataUser

data class CommentWithUserProfile(
    val comment: DataCommentItem,
    val userProfile: DataUser
)
