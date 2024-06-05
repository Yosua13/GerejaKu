package com.example.projecttingkat2.firebase

import android.util.Log
import com.example.projecttingkat2.model.Buku
import com.example.projecttingkat2.model.Gereja
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BukuRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("buku")

    suspend fun insert(buku: Buku): String {
        val document = collection.document()
        buku.id = document.id
        document.set(buku).await()
        Log.d("BukuRepository", "Inserted buku with id: ${buku.id}")
        return buku.id
    }

    suspend fun update(buku: Buku) {
        val document = collection.document(buku.id)
        document.set(buku).await()
        Log.d("BukuRepository", "Updated buku with id: ${buku.id}")
    }

    suspend fun delete(id: String) {
        val document = collection.document(id)
        document.delete().await()
        Log.d("BukuRepository", "Deleted buku with id: $id")
    }

    suspend fun getBukuById(id: String): Buku? {
        val document = collection.document(id).get().await()
        val buku = document.toObject(Buku::class.java)
        Log.d("BukuRepository", "Fetched buku with id: $id")
        return buku
    }

    suspend fun getAllBuku(): List<Buku> {
        return try {
            val documents = collection.get().await().documents
            Log.d("BukuRepository", "Fetched ${documents.size} buku items")
            documents.mapNotNull { it.toObject(Buku::class.java) }
        } catch (e: Exception) {
            Log.e("BukuRepository", "Error fetching buku items", e)
            emptyList()
        }
    }

    suspend fun getNextId(): String {
        val document = collection.document()
        return document.id
    }

    suspend fun getBukuList(): List<Buku> {
        val snapshot = collection.get().await()
        return snapshot.documents.map { document ->
            document.toObject(Buku::class.java)!!.copy(id = document.id)
        }
    }
}