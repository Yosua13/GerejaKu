package com.example.projecttingkat2.firebase

import android.util.Log
import com.example.projecttingkat2.model.BeritaAcara
import com.example.projecttingkat2.model.Gereja
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PenggunaBeritaRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("berita_acara")

    suspend fun getBeritaById(id: String): BeritaAcara? {
        val document = collection.document(id).get().await()
        val berita = document.toObject(BeritaAcara::class.java)
        Log.d("BeritaRepository", "Fetched berita with id: $id")
        return berita
    }
}