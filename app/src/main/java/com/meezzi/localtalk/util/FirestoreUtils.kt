package com.meezzi.localtalk.util

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.meezzi.localtalk.data.Post
import kotlinx.coroutines.tasks.await

object FirestoreUtils {

    suspend fun getPostById(
        city: String,
        categoryId: String,
        postId: String
    ): Post? {
        return try {
            val postRef = Firebase.firestore.collection("posts")
                .document(city)
                .collection(categoryId)
                .document(postId)

            val documentSnapshot = postRef.get().await()
            documentSnapshot.toObject<Post>()
        } catch (e: Exception) {
            throw e
        }
    }
}