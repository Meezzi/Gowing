package com.meezzi.localtalk.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.meezzi.localtalk.data.Categories
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.data.User
import com.meezzi.localtalk.util.FirestoreUtils.getPostById
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    auth: FirebaseAuth,
) {

    private val currentUser = auth.currentUser

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
        val imageRef = storage.reference.child("images/${currentUser?.uid}_profile_image")
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
            val profileImageRef =
                storage.reference.child("images/${currentUser?.uid}_profile_image")

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

    suspend fun fetchLikedPosts(): List<Post> {
        val userId = currentUser?.uid ?: return emptyList()
        val likedPosts = mutableListOf<Post>()

        try {
            val documentSnapshot = db.collection("profiles").document(userId).get().await()
            val likedPostList =
                documentSnapshot["likedPostList"] as? List<Map<String, String>> ?: emptyList()

            likedPostList.forEach { likedPostData ->
                val city = likedPostData["city"] ?: return@forEach
                val categoryId = likedPostData["categoryId"] ?: return@forEach
                val postId = likedPostData["postId"] ?: return@forEach

                val post = getPostById(city, categoryId, postId)
                post?.let { likedPosts.add(it) }
            }
        } catch (e: Exception) {
            throw e
        }

        return likedPosts
    }
}