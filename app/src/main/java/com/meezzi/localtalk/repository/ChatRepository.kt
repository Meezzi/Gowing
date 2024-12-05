package com.meezzi.localtalk.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.meezzi.localtalk.data.ChatRoom

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
}