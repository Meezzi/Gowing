package com.meezzi.localtalk.repository

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.meezzi.localtalk.data.ChatRoom
import com.meezzi.localtalk.data.Message
import kotlinx.coroutines.tasks.await

class ChatRepository {

    private val db = FirebaseFirestore.getInstance()
    private val currentUserId = Firebase.auth.currentUser?.uid

    fun getCurrentUserId(): String = currentUserId.toString()

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

    fun fetchMessages(
        chatRoomId: String,
        onResult: (List<Message>) -> Unit,
    ) {
        db.collection("chat_rooms")
            .document(chatRoomId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull { document ->
                    document.toObject<Message>()
                } ?: emptyList()
                onResult(messages)
            }
    }

    suspend fun getUserNickname(chatRoomId: String, onResult: (String) -> Unit) {
        val otherUserId = fetchOtherUserId(chatRoomId)
        val otherUserNickname = fetchNickname(otherUserId)
        onResult(otherUserNickname)
    }

    private suspend fun fetchOtherUserId(chatRoomId: String): String {
        return try {
            val document = db.collection("chat_rooms").document(chatRoomId).get().await()
            if (document.exists()) {
                val participants = document.get("participants") as? List<String> ?: emptyList()
                participants.firstOrNull { it != currentUserId } ?: "상대방을 찾을 수 없습니다."
            } else {
                "채팅방 정보를 찾을 수 없습니다."
            }
        } catch (e: Exception) {
            "오류가 발생하였습니다."
        }
    }

    private suspend fun fetchNickname(userId: String): String {
        return try {
            val document = db.collection("profiles").document(userId).get().await()
            if (document.exists()) {
                document.getString("nickname") ?: "닉네임 없음"
            } else {
                "사용자 정보를 찾을 수 없습니다."
            }
        } catch (e: Exception) {
            "오류가 발생하였습니다."
        }
    }

    suspend fun fetchProfileImageByUserId(chatRoomId: String, onResult: (Uri?) -> Unit,) {
        val otherUserId = fetchOtherUserId(chatRoomId)
        val profileImageRef = Firebase.storage.reference.child("images/${otherUserId}_profile_image")

        profileImageRef.downloadUrl.addOnSuccessListener { uri ->
            onResult(uri)
        }.addOnFailureListener { e->
            onResult(null)
        }
    }

    suspend fun uploadImageToFirebase(chatRoomId: String, uri: Uri): String {
        val uid = fetchOtherUserId(chatRoomId)
        val imageRef = Firebase.storage.reference.child("chat_images/$chatRoomId/${uid}/${System.currentTimeMillis()}.jpg")
        imageRef.putFile(uri).await()

        return imageRef.downloadUrl.await().toString()
    }
}