package com.example.projecttingkat2.firebase

import com.example.projecttingkat2.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    suspend fun getUserById(userId: String): User? {
        val userDoc = firestore.collection("users").document(userId).get().await()
        return userDoc.toObject(User::class.java)
    }
}