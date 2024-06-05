package com.example.projecttingkat2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttingkat2.firebase.BukuRepository
import com.example.projecttingkat2.model.Buku
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BukuDetailViewModel(private val repository: BukuRepository) : ViewModel() {
    private val _bukuList = MutableStateFlow<List<Buku>>(emptyList())
    val bukuList: StateFlow<List<Buku>> get() = _bukuList

    // StateFlow untuk data buku yang sedang diedit
    private val _editedBuku = MutableStateFlow(Buku())

    init {
        Log.d("BukuDetailViewModel", "ViewModel initialized")
        fetchBukuList()
    }

    private fun fetchBukuList() {
        viewModelScope.launch {
            Log.d("BukuDetailViewModel", "Fetching buku list")
            try {
                val list = repository.getAllBuku()
                _bukuList.value = list
                list.forEach { buku ->
                    Log.d("BookScreen", "Fetched buku: ${buku.judul}, URL: ${buku.gambar}")
                }
                Log.d("BukuDetailViewModel", "Fetched ${list.size} items")
            } catch (e: Exception) {
                Log.d("BukuDetailViewModel", "Error fetching data: ${e.message}")
            }
        }
    }

    fun insert(judul: String, penulis: String, sinopsis: String, gambar: String) {
        viewModelScope.launch {
            val id = repository.getNextId()
            val buku = Buku(id, judul, penulis, sinopsis, gambar)
            repository.insert(buku)
            fetchBukuList()
            Log.d("BukuDetailViewModel", "Inserted buku with id: $id")
        }
    }

    fun update(id: String, judul: String, penulis: String, sinopsis: String, gambar: String) {
        viewModelScope.launch {
            val buku = Buku(id, judul, penulis, sinopsis, gambar)
            repository.update(buku)
            fetchBukuList()
            _editedBuku.value = buku
            Log.d("BukuDetailViewModel", "Updated buku with id: $id")
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            repository.delete(id)
            fetchBukuList()
            Log.d("BukuDetailViewModel", "Deleted buku with id: $id")
        }
    }

    suspend fun getBukuById(id: String): Buku? {
        Log.d("BukuDetailViewModel", "Fetching buku with id: $id")
        return repository.getBukuById(id)
    }

    fun searchBuku(keyword: String) {
        viewModelScope.launch {
            _bukuList.value = if (keyword.isNotEmpty()) {
                // Jika ada kata kunci, filter daftar gereja sesuai dengan kata kunci
                bukuList.value.filter { buku ->
                    buku.judul.contains(keyword, ignoreCase = true)
                }
            } else {
                // Jika tidak ada kata kunci, tampilkan seluruh daftar gereja
                repository.getBukuList()
            }
        }
    }
}