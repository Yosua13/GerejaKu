package com.example.projecttingkat2.firebase

import android.util.Log
import com.example.projecttingkat2.model.Buku
import com.example.projecttingkat2.model.Gereja
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PenggunaBukuRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("buku")

    suspend fun getBukuById(id: String): Buku? {
        val document = collection.document(id).get().await()
        val buku = document.toObject(Buku::class.java)
        Log.d("BukuRepository", "Fetched buku with id: $id")
        return buku
    }
}