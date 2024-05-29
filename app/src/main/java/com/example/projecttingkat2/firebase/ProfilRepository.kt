package com.example.projecttingkat2.firebase

import android.util.Log
import com.example.projecttingkat2.model.Gereja
import com.example.projecttingkat2.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfilRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun updateProfile(
        userId: String,
        firstName: String,
        lastName: String,
        email: String,
        newPassword: String? = null
    ) {
        try {
            // Update data pengguna di Firestore
            val userUpdate = mapOf(
                "firstName" to firstName,
                "lastName" to lastName,
                "email" to email
            )
            firestore.collection("users").document(userId).update(userUpdate).await()

            val firebaseUser = auth.currentUser
            firebaseUser?.let {
                // Update data pengguna di Firebase Authentication
                it.updateProfile(userProfileChangeRequest {
                    displayName = "$firstName $lastName"
                }).await()

                it.updateEmail(email).await()

                // Update password in Firebase Authentication if provided
                if (!newPassword.isNullOrEmpty()) {
                    it.updatePassword(newPassword).await()
                }
            } ?: throw Exception("FirebaseUser is null")
        } catch (e: Exception) {
            throw e
        }
    }
}