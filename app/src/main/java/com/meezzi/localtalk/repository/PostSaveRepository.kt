package com.meezzi.localtalk.repository

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import com.meezzi.localtalk.data.Comment
import com.meezzi.localtalk.data.Post

class PostSaveRepository {

    private val db = Firebase.firestore
    private val storageRef = Firebase.storage.reference

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    fun savePostWithImages(
        post: Post,
        imageUris: List<Uri>?,
        onSuccess: (String, String, String) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        val postId = db.collection("posts").document().id

        if (!imageUris.isNullOrEmpty()) {
            imageUris.forEach { uri ->
                uploadImage(uri, postId)
            }
        }
        savePost(
            postId = postId,
            post = post,
            onSuccess = { onSuccess(post.city, post.category.id, postId) },
            onFailure = onFailure
        )
    }

    private fun uploadImage(
        uri: Uri,
        postId: String
    ) {

        val timeStamp = System.currentTimeMillis().toString()

        val imagesRef = storageRef.child("posts/${currentUser?.uid}/${postId}/$timeStamp.png")
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

    fun plusLikeCount(
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

        val profileImageRef = storageRef.child("images/${authorId}_profile_image")
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

        commentRef.get()
            .addOnSuccessListener { document ->
                val commentList = document.documents.mapNotNull { document ->
                    document.toObject<Comment>()?.copy(commentId = document.id)
                }
                onSuccess(commentList)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
}