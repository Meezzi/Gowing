package com.meezzi.localtalk.data

import com.google.firebase.Timestamp

data class Message(
    val senderId: String = "",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val type: String = "",
)