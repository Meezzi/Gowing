package com.meezzi.localtalk.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import com.meezzi.localtalk.data.Categories
import com.meezzi.localtalk.data.Post
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class BoardRepository {

    private val db = Firebase.firestore
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    fun fetchPostsByCategory(
        city: String,
        category: String,
        onSuccess: (List<Post>) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        val docRef = db.collection("posts/${city}/${category}")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val topPosts = document.toObjects<Post>()
                    onSuccess(topPosts)
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    suspend fun fetchMyPosts(
        onSuccess: (List<Post>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val categoryCollections = Categories.entries.map { it.name.lowercase() }

        try {
            val allPosts = coroutineScope {
                categoryCollections.map { collectionName ->
                    db.collectionGroup(collectionName)
                        .whereEqualTo("authorId", currentUser?.uid)
                        .get()
                        .await()
                        .toObjects<Post>()
                }.flatten()
            }
            onSuccess(allPosts)
        } catch (exception: Exception) {
            onFailure(exception)
        }
    }
}