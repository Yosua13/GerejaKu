package com.example.projecttingkat2.firebase

import android.util.Log
import com.example.projecttingkat2.model.Gereja
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PenggunaGerejaRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("gereja")

    suspend fun getGerejaById(id: String): Gereja? {
        val document = collection.document(id).get().await()
        val gereja = document.toObject(Gereja::class.java)
        Log.d("GerejaRepository", "Fetched gereja with id: $id")
        return gereja
    }
}