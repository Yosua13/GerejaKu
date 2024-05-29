package com.example.projecttingkat2.firebase

import android.util.Log
import com.example.projecttingkat2.model.BeritaAcara
import com.example.projecttingkat2.model.Gereja
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BeritaAcaraRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("berita_acara")

    suspend fun insert(beritaAcara: BeritaAcara): String {
        val document = collection.document()
        beritaAcara.id = document.id
        document.set(beritaAcara).await()
        Log.d("GerejaRepository", "Inserted gereja with id: ${beritaAcara.id}")
        return beritaAcara.id
    }

    suspend fun update(beritaAcara: BeritaAcara) {
        val document = collection.document(beritaAcara.id)
        document.set(beritaAcara).await()
        Log.d("BeritaAcaraRepository", "Updated berita acara with id: ${beritaAcara.id}")
    }

    suspend fun delete(id: String) {
        val document = collection.document(id)
        document.delete().await()
        Log.d("BeritaAcaraRepository", "Deleted berita acara with id: $id")
    }

    suspend fun getBeritaAcaraById(id: String): BeritaAcara? {
        val document = collection.document(id).get().await()
        val beritaAcara = document.toObject(BeritaAcara::class.java)
        Log.d("BeritaAcaraRepository", "Fetched berita acara with id: $id")
        return beritaAcara
    }

    suspend fun getAllBeritaAcara(): List<BeritaAcara> {
        return try {
            val documents = collection.get().await().documents
            Log.d("BeritaAcaraRepository", "Fetched ${documents.size} berita acara items")
            documents.mapNotNull { it.toObject(BeritaAcara::class.java) }
        } catch (e: Exception) {
            Log.e("BeritaAcaraRepository", "Error fetching berita acara items", e)
            emptyList()
        }
    }

    suspend fun getNextId(): String {
        val document = collection.document()
        return document.id
    }
}