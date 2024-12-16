package com.meezzi.localtalk.repository

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import com.meezzi.localtalk.data.Categories
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.data.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    val db = Firebase.firestore
    val storage = Firebase.storage
    var storageRef = storage.reference

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    fun fetchUserEmail(): String {
        return currentUser?.email ?: "이메일을 찾을 수 없습니다."
    }

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

    private fun saveProfileImage(profileImageUri: Uri?) {
        val imageRef = storageRef.child("images/${currentUser?.uid}_profile_image")
        val uploadTask = profileImageUri?.let { imageRef.putFile(it) }

        uploadTask?.addOnFailureListener {

        }?.addOnSuccessListener { taskSnapshot ->

        }
    }

    fun saveProfileData(nickname: String, profileImageUri: Uri?, onComplete: (Boolean) -> Unit) {
        val profile = User(
            userId = currentUser?.uid.toString(),
            nickname = nickname,
            profileImageUrl = profileImageUri,
        )

        saveProfileImage(profileImageUri)

        currentUser?.let { user ->
            db.collection("profiles")
                .document(user.uid)
                .set(profile)
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } ?: onComplete(false)
    }

    fun getProfileImageUri(onComplete: (Uri?) -> Unit) {
        currentUser?.let { user ->
            val profileImageRef = storageRef.child("images/${currentUser?.uid}_profile_image")

            profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                onComplete(uri)
            }.addOnFailureListener {
                onComplete(null)
            }
        } ?: onComplete(null)
    }

    fun getProfileData(onComplete: (String) -> Unit) {

        currentUser?.let { user ->
            db.collection("profiles")
                .document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nickname = document.getString("nickname") ?: ""
                        onComplete(nickname)
                    } else {
                        onComplete("")
                    }
                }
                .addOnFailureListener {
                    onComplete("")
                }
        } ?: onComplete("")
    }

    suspend fun fetchMyPosts(
        city: String,
        onComplete: (List<Post>) -> Unit,
    ) {
        val userId = currentUser?.uid
        val categoryIds = Categories.entries.map { it.name.lowercase() }
        val posts = mutableListOf<Post>()

        for (categoryId in categoryIds) {
            val querySnapshot =
                db.collection("posts").document(city.split(" ")[0]).collection(categoryId)
                    .whereEqualTo("authorId", userId)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()

            for (document in querySnapshot) {
                val post = document.toObject<Post>()
                posts.add(post)
            }
        }
        onComplete(posts)
    }
}