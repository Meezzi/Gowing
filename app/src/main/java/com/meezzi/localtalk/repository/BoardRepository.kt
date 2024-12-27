package com.meezzi.localtalk.repository

 import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.meezzi.localtalk.data.Categories
import com.meezzi.localtalk.data.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BoardRepository @Inject constructor(
    private val db: FirebaseFirestore,
    auth: FirebaseAuth,
) {

    private val currentUser = auth.currentUser

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

    suspend fun fetchPostsWithMyComments(
        onSuccess: (List<Post>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            val postIds = db.collectionGroup("comments")
                .whereEqualTo("authorId", currentUser?.uid)
                .get()
                .await()
                .documents
                .mapNotNull { it.getString("postId") }
                .distinct()

            if (postIds.isEmpty()) {
                onSuccess(emptyList())
                return
            }

            val categoryCollections = Categories.entries.map { it.name.lowercase() }

            val posts = coroutineScope {
                postIds.map { postId ->
                    async {
                        categoryCollections.flatMap { category ->
                            db.collectionGroup(category)
                                .whereEqualTo("postId", postId)
                                .get()
                                .await()
                                .toObjects<Post>()
                        }
                    }
                }.flatMap { it.await() }
            }
            onSuccess(posts)
        } catch (e: Exception) {
            onFailure(e)
        }
    }
}