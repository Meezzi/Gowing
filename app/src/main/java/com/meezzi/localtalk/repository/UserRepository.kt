package com.meezzi.localtalk.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.meezzi.localtalk.data.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    val db = Firebase.firestore

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    suspend fun isNicknameDuplicate(nickname: String): Boolean {
        return try {
            val querySnapshot = db.collection("profiles")
                .whereEqualTo("nickname", nickname)
                .get()
                .await()
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    fun saveProfileData(nickname: String, onComplete: (Boolean) -> Unit) {
        val profile = User(
            userId = currentUser?.uid.toString(),
            nickname = nickname,
            profileImageUrl = null,
        )

        currentUser?.let { user ->
            db.collection("profiles")
                .document(user.uid)
                .set(profile)
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } ?: onComplete(false)
    }

    fun getProfileData(onComplete: (String, String) -> Unit) {

        currentUser?.let { user ->
            db.collection("profiles")
                .document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nickname = document.getString("nickname") ?: ""
                        val profileImageUrl = document.getString("profileImageUrl") ?: ""

                        onComplete(nickname, profileImageUrl)
                    } else {
                        onComplete("", "")
                    }
                }
                .addOnFailureListener {
                    onComplete("", "")
                }
        } ?: onComplete("", "")
    }
}