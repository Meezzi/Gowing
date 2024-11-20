package com.meezzi.localtalk.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import com.meezzi.localtalk.data.Post

class BoardRepository {

    private val db = Firebase.firestore

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
}