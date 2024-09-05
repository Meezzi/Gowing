package com.meezzi.localtalk.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.meezzi.localtalk.data.User

class UserRepository {

    val db = Firebase.firestore

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

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
}