package com.meezzi.localtalk.repository

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage

class PostSaveRepository {

    private val storageRef = Firebase.storage.reference

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

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
}