package com.meezzi.localtalk.repository

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.meezzi.localtalk.data.Post

class PostSaveRepository {

    private val db = Firebase.firestore
    private val storageRef = Firebase.storage.reference

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    fun savePostWithImages(
        post: Post,
        imageUris: List<Uri>?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        val postId = db.collection("posts").document().id

        if (!imageUris.isNullOrEmpty()) {
            imageUris.forEach { uri ->
                uploadImage(uri, postId)
            }
        }
        savePost(postId, post, onSuccess, onFailure)
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
}