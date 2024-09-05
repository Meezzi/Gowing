package com.meezzi.localtalk.data

data class User(
    var userId: String,
    var nickname: String,
    var profileImageUrl: String? = null
)