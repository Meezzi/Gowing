package com.meezzi.localtalk.repository

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
        imageUrls: List<String> = emptyList(),
    ) {
        val messageData = Message(
            senderId = currentUserId ?: "",
            content = messageContent,
            timestamp = Timestamp.now(),
            type = if (imageUrls.isEmpty()) "text" else "image",
            imageUrl = imageUrls
        )

        val chatRoomRef = db.collection("chat_rooms").document(chatRoomId)

        chatRoomRef.collection("messages")
            .add(messageData)
            .addOnSuccessListener {
                chatRoomRef.update(
                    mapOf(
                        "lastMessage" to if (messageData.type == "image") "[사진]" else messageData.content,
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

    fun observeNickname(userId: String, onResult: (String) -> Unit) {
        db.collection("profiles")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                val nickname = snapshot?.getString("nickname") ?: "닉네임 없음"
                onResult(nickname)
            }
    }

    suspend fun fetchOtherUserId(chatRoomId: String): String {
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

    fun observeProfileImage(userId: String, onResult: (Uri?) -> Unit) {
        val profileImageRef = Firebase.storage.reference.child("images/${userId}_profile_image")

        db.collection("profiles")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onResult(null)
                    return@addSnapshotListener
                }

                profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                    onResult(uri)
                }.addOnFailureListener {
                    onResult(null)
                }
            }
    }

    suspend fun uploadImageToFirebase(chatRoomId: String, uri: Uri): String {
        val uid = fetchOtherUserId(chatRoomId)
        val imageRef =
            Firebase.storage.reference.child("chat_images/$chatRoomId/${uid}/${System.currentTimeMillis()}.jpg")
        imageRef.putFile(uri).await()

        return imageRef.downloadUrl.await().toString()
    }

    suspend fun fetchChatRoomList(): List<ChatRoom> {
        val userId = getCurrentUserId()

        val querySnapshot = db.collection("chat_rooms")
            .whereArrayContains("participants", userId)
            .orderBy("lastMessageTime", Query.Direction.DESCENDING)
            .get()
            .await()

        return querySnapshot.documents.mapNotNull { document ->
            document.toObject<ChatRoom>()
        }
    }

    suspend fun fetchParticipantInfo(chatRoom: ChatRoom): Pair<String, Uri?> {
        val otherUserId =
            chatRoom.participants.find { it != currentUserId } ?: return "알 수 없는 사용자" to null
        val nickname = fetchNickname(otherUserId)
        val profileImageUri = try {
            Firebase.storage.reference.child("images/${otherUserId}_profile_image").downloadUrl.await()
        } catch (e: Exception) {
            null
        }
        return nickname to profileImageUri
    }

    suspend fun deleteChatRoomParticipant(
        chatRoomId: String,
        participantId: String
    ) {
        val docRef = db.collection("chat_rooms").document(chatRoomId)

        try {
            docRef.update("participants", FieldValue.arrayRemove(participantId)).await()

            val participantCount = getParticipantCount(chatRoomId)
            when (participantCount) {
                1 -> docRef.update("isActive", false).await()
                0 -> deleteChatRoom(chatRoomId)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun getParticipantCount(chatRoomId: String): Int {
        return try {
            val document = db.collection("chat_rooms").document(chatRoomId).get().await()
            val chatRoom = document.toObject<ChatRoom>()
            chatRoom?.participants?.size ?: 0
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun deleteChatRoom(chatRoomId: String) {
        val docRef = db.collection("chat_rooms").document(chatRoomId)
        docRef.delete().await()
        val updates = hashMapOf<String, Any>(
            "messages" to FieldValue.delete()
        )
        docRef.update(updates)
    }

    fun observeIsActive(
        chatRoomId: String,
        onResult: (Boolean) -> Unit,
    ) {
        val chatRoomRef = db.collection("chat_rooms").document(chatRoomId)
        chatRoomRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val isActive = snapshot.getBoolean("isActive") ?: true
                onResult(isActive)
            } else {
                onResult(false)
            }
        }
    }
}