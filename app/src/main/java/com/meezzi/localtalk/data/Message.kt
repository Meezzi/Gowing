package com.meezzi.localtalk.data

import com.google.firebase.Timestamp

data class Message(
    val senderId: String = "",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val type: String = "",
    val imageUrl: List<String> = emptyList(),
)

data class ChatRoom(
    val chatRoomId: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageTime: Timestamp? = null
)