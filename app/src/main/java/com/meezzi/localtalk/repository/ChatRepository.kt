package com.meezzi.localtalk.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.meezzi.localtalk.data.ChatRoom
import com.meezzi.localtalk.data.Message

class ChatRepository {

    private val db = FirebaseFirestore.getInstance()
    private val currentUserId = Firebase.auth.currentUser?.uid

    fun getOrCreateChatRoom(
        authorId: String,
        onResult: (String) -> Unit,
    ) {
        val chatRoomsCollection = db.collection("chat_rooms")
        val participants = listOf(currentUserId.toString(), authorId).sorted()

        chatRoomsCollection
            .whereEqualTo("participants", participants)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    val chatRoomId = chatRoomsCollection.document().id
                    val newChatRoom = ChatRoom(chatRoomId = chatRoomId, participants = participants)

                    chatRoomsCollection
                        .document(chatRoomId)
                        .set(newChatRoom)
                        .addOnSuccessListener { onResult(chatRoomId) }
                } else {
                    val chatRoomId = documents.first().id
                    onResult(chatRoomId)
                }
            }
    }

    fun sendMessage(
        chatRoomId: String,
        messageContent: String,
    ) {
        val messageData = Message(
            senderId = currentUserId!!,
            content = messageContent,
            timestamp = Timestamp.now()
        )

        val chatRoomRef = db.collection("chat_rooms").document(chatRoomId)

        chatRoomRef.collection("messages")
            .add(messageData)
            .addOnSuccessListener {
                chatRoomRef.update(
                    mapOf(
                        "lastMessage" to messageContent,
                        "lastMessageTime" to messageData.timestamp
                    )
                )
            }
    }
}