package com.meezzi.localtalk.data

import android.net.Uri

data class User(
    var userId: String,
    var nickname: String,
    var profileImageUrl: Uri? = null
)