package com.example.projecttingkat2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttingkat2.firebase.GerejaRepository
import com.example.projecttingkat2.model.Gereja
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GerejaDetailViewModel(private val repository: GerejaRepository) : ViewModel() {
    private val _gerejaList = MutableStateFlow<List<Gereja>>(emptyList())
    val gerejaList: StateFlow<List<Gereja>> get() = _gerejaList

    // StateFlow untuk data gereja yang sedang diedit
    private val _editedGereja = MutableStateFlow(Gereja())

    init {
        Log.d("GerejaDetailViewModel", "ViewModel initialized")
        fetchGerejaList()
    }

    private fun fetchGerejaList() {
        viewModelScope.launch {
            Log.d("GerejaDetailViewModel", "Fetching gereja list")
            try {
                val list = repository.getAllGereja()
                _gerejaList.value = list
                Log.d("GerejaDetailViewModel", "Fetched ${list.size} items")
            } catch (e: Exception) {
                Log.d("GerejaDetailViewModel", "Error fetching data: ${e.message}")
            }
        }
    }

    fun insert(judul: String, aliran: String, lokasi: String, link: String, jadwal: String, deskripsi: String, gambar: String) {
        viewModelScope.launch {
            val id = repository.getNextId()
            val gereja = Gereja(id, judul, aliran, lokasi, link, jadwal, deskripsi, gambar)
            repository.insert(gereja)
            fetchGerejaList()
            Log.d("GerejaDetailViewModel", "Inserted gereja with id: $id")
        }
    }

    fun update(id: String, judul: String, aliran: String, lokasi: String, link: String, jadwal: String, deskripsi: String, gambar: String) {
        viewModelScope.launch {
            val gereja = Gereja(id, judul, aliran, lokasi, link, jadwal, deskripsi, gambar)
            repository.update(gereja)
            fetchGerejaList()
            _editedGereja.value = gereja
            Log.d("GerejaDetailViewModel", "Updated gereja with id: $id")
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            repository.delete(id)
            fetchGerejaList()
            Log.d("GerejaDetailViewModel", "Deleted gereja with id: $id")
        }
    }

    suspend fun getGerejaById(id: String): Gereja? {
        Log.d("GerejaDetailViewModel", "Fetching gereja with id: $id")
        return repository.getGerejaById(id)
    }
}