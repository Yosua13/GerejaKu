package com.example.projecttingkat2.firebase

import android.util.Log
import com.example.projecttingkat2.model.Gereja
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class GerejaRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("gereja")

    suspend fun insert(gereja: Gereja): String {
        val document = collection.document()
        gereja.id = document.id
        document.set(gereja).await()
        Log.d("GerejaRepository", "Inserted gereja with id: ${gereja.id}")
        return gereja.id
    }

    suspend fun update(gereja: Gereja) {
        val document = collection.document(gereja.id)
        document.set(gereja).await()
        Log.d("GerejaRepository", "Updated gereja with id: ${gereja.id}")
    }

    suspend fun delete(id: String) {
        val document = collection.document(id)
        document.delete().await()
        Log.d("GerejaRepository", "Deleted gereja with id: $id")
    }

    suspend fun getGerejaById(id: String): Gereja? {
        val document = collection.document(id).get().await()
        val gereja = document.toObject(Gereja::class.java)
        Log.d("GerejaRepository", "Fetched gereja with id: $id")
        return gereja
    }

    suspend fun getAllGereja(): List<Gereja> {
        return try {
            val documents = collection.get().await().documents
            Log.d("GerejaRepository", "Fetched ${documents.size} gereja items")
            documents.mapNotNull { it.toObject(Gereja::class.java) }
        } catch (e: Exception) {
            Log.e("GerejaRepository", "Error fetching gereja items", e)
            emptyList()
        }
    }

    suspend fun getNextId(): String {
        val document = collection.document()
        return document.id
    }
}