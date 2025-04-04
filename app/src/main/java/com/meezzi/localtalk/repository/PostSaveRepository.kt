package com.meezzi.localtalk.repository

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.meezzi.localtalk.data.Comment
import com.meezzi.localtalk.data.Post
import java.util.Calendar
import javax.inject.Inject

class PostSaveRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    auth: FirebaseAuth,
) {

    private val currentUser = auth.currentUser

    fun savePostWithImages(
        post: Post,
        imageUris: List<Uri>?,
        isAnonymous: Boolean,
        onSuccess: (String, String, String) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        val postId = db.collection("posts").document().id
        val userId = currentUser?.uid ?: ""

        if (isAnonymous) {
            savePost(
                postId = postId,
                post = post.copy(authorName = "익명"),
                onSuccess = { onSuccess(post.city, post.category.id, postId) },
                onFailure = onFailure
            )
        } else {
            db.collection("profiles")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val nickname = document?.getString("nickname") ?: "닉네임 없음"

                    if (!imageUris.isNullOrEmpty()) {
                        imageUris.forEach { uri ->
                            uploadImage(uri, post.city, post.category.id, postId)
                        }
                    }

                    savePost(
                        postId = postId,
                        post = post.copy(authorName = nickname),
                        onSuccess = { onSuccess(post.city, post.category.id, postId) },
                        onFailure = onFailure
                    )
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        }
    }

    private fun uploadImage(
        uri: Uri,
        city: String,
        categoryId: String,
        postId: String
    ) {

        val timeStamp = System.currentTimeMillis().toString()

        val imagesRef =
            storage.reference.child("posts/${city}/${categoryId}/${postId}/$timeStamp.png")
        val uploadTask = imagesRef.putFile(uri)

        uploadTask.addOnFailureListener {

        }.addOnSuccessListener { taskSnapshot ->

        }
    }

    private fun savePost(
        postId: String,
        post: Post,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val postToSave = post.copy(
            postId = postId,
            authorId = currentUser?.uid
        )

        db.collection("posts")
            .document(post.city)
            .collection(post.category.id)
            .document(postId)
            .set(postToSave)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getPostById(
        city: String,
        categoryId: String,
        postId: String,
        onSuccess: (Post) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val postRef = db.collection("posts")
            .document(city)
            .collection(categoryId)
            .document(postId)

        postRef.get()
            .addOnSuccessListener { document ->
                val post = document.toObject<Post>()
                if (post != null) {
                    onSuccess(post)
                } else {
                    onFailure(Exception("게시물을 찾을 수 없습니다."))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun getLikeCount(
        postId: String,
        city: String,
        categoryId: String,
        onComplete: (Int) -> Unit,
    ) {
        val postRef = db.collection("posts")
            .document(city)
            .collection(categoryId)
            .document(postId)

        postRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val likes = documentSnapshot.getLong("likes")?.toInt() ?: 0
                    onComplete(likes)
                }
            }
    }

    private fun plusLikeCount(
        postId: String,
        city: String,
        categoryId: String
    ) {
        db.collection("posts")
            .document(city)
            .collection(categoryId)
            .document(postId)
            .update("likes", FieldValue.increment(1))
    }

    fun addLikedPost(
        postId: String,
        city: String,
        categoryId: String,
    ) {
        val userId = currentUser?.uid ?: ""

        val likedPostData = mapOf(
            "postId" to postId,
            "city" to city,
            "categoryId" to categoryId
        )

        db.collection("profiles").document(userId)
            .update("likedPostList", FieldValue.arrayUnion(likedPostData))
            .addOnSuccessListener {
                plusLikeCount(postId, city, categoryId)
            }
            .addOnFailureListener { e ->
                throw e
            }
    }

    fun minusLikeCount(
        postId: String,
        city: String,
        categoryId: String
    ) {
        db.collection("posts")
            .document(city)
            .collection(categoryId)
            .document(postId)
            .update("likes", FieldValue.increment(-1))
    }

    fun getProfileImageUri(authorId: String, onComplete: (Uri?) -> Unit) {

        val profileImageRef = storage.reference.child("images/${authorId}_profile_image")
        profileImageRef.downloadUrl.addOnSuccessListener { uri ->
            onComplete(uri)
        }.addOnFailureListener {
            onComplete(null)
        }
    }

    fun saveComment(
        city: String,
        categoryId: String,
        postId: String,
        comment: Comment,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {

        val postRef = db.collection("posts")
            .document(city)
            .collection(categoryId)
            .document(postId)

        val commentRef = postRef.collection("comments").document()
        val commentData = comment.copy(commentId = commentRef.id)

        commentRef
            .set(commentData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getComments(
        city: String,
        categoryId: String,
        postId: String,
        onSuccess: (List<Comment>) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {

        val commentRef = db.collection("posts")
            .document(city)
            .collection(categoryId)
            .document(postId)
            .collection("comments")
            .orderBy("date", Query.Direction.ASCENDING)
            .orderBy("time", Query.Direction.ASCENDING)

        commentRef.get()
            .addOnSuccessListener { document ->
                val commentList = document.documents.mapNotNull { document ->
                    document.toObject<Comment>()?.copy(commentId = document.id)
                }
                onSuccess(commentList)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getCommentLikeCount(
        postId: String,
        city: String,
        categoryId: String,
        commentId: String,
        onComplete: (Int) -> Unit,
    ) {
        val commentsRef = db.collection("posts")
            .document(city)
            .collection(categoryId)
            .document(postId)
            .collection("comments")
            .document(commentId)

        commentsRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val likes = document.getLong("likes")?.toInt() ?: 0
                    onComplete(likes)
                }
            }
    }

    fun plusCommentLikeCount(
        postId: String,
        city: String,
        categoryId: String,
        commentId: String,
    ) {
        val commentsRef = db.collection("posts")
            .document(city)
            .collection(categoryId)
            .document(postId)
            .collection("comments")
            .document(commentId)

        commentsRef
            .update("likes", FieldValue.increment(1))
    }

    fun minusCommentLikeCount(
        postId: String,
        city: String,
        categoryId: String,
        commentId: String,
    ) {
        val commentsRef = db.collection("posts")
            .document(city)
            .collection(categoryId)
            .document(postId)
            .collection("comments")
            .document(commentId)

        commentsRef
            .update("likes", FieldValue.increment(-1))
    }

    fun getHotPosts(
        city: String,
        onSuccess: (List<Post>) -> Unit,
    ) {
        if (city.isBlank()) return

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val oneWeekAgo = Timestamp(calendar.time)

        db.collection("posts/$city/free_board")
            .whereGreaterThanOrEqualTo("timestamp", oneWeekAgo)
            .orderBy("likes", Query.Direction.DESCENDING)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener { documents ->
                val topPosts = documents.toObjects<Post>()
                onSuccess(topPosts)
            }
    }

    fun getLatestPosts(
        city: String,
        onSuccess: (List<Post>) -> Unit,
    ) {
        if (city.isBlank()) return

        db.collection("posts/$city/free_board")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener { documents ->
                val topPosts = documents.toObjects<Post>()
                onSuccess(topPosts)
            }
    }
}