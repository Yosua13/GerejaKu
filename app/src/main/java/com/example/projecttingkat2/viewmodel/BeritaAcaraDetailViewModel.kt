package com.example.projecttingkat2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttingkat2.firebase.BeritaAcaraRepository
import com.example.projecttingkat2.firebase.GerejaRepository
import com.example.projecttingkat2.model.BeritaAcara
import com.example.projecttingkat2.model.Gereja
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BeritaAcaraDetailViewModel(private val repository: BeritaAcaraRepository) : ViewModel() {
    private val _beritaList = MutableStateFlow<List<BeritaAcara>>(emptyList())
    val beritaList: StateFlow<List<BeritaAcara>> get() = _beritaList

    // StateFlow untuk data gereja yang sedang diedit
    private val _editedBerita = MutableStateFlow(BeritaAcara())

    init {
        Log.d("BeritaAcaraDetailViewModel", "ViewModel initialized")
        fetchBeritaAcaraList()
    }

    private fun fetchBeritaAcaraList() {
        viewModelScope.launch {
            Log.d("BeritaAcaraDetailViewModel", "Fetching berita acara list")
            try {
                val list = repository.getAllBeritaAcara()
                _beritaList.value = list
                Log.d("BeritaAcaraDetailViewModel", "Fetched ${list.size} items")
            } catch (e: Exception) {
                Log.d("BeritaAcaraDetailViewModel", "Error fetching data: ${e.message}")
            }
        }
    }

    fun insert(judul: String, namaGereja: String, pembicara: String, jadwalIbadah: String, deskripsi: String, gambarBerita: String) {
        viewModelScope.launch {
            val id = repository.getNextId()
            val beritaAcara = BeritaAcara(id, judul, namaGereja, pembicara, jadwalIbadah, deskripsi, gambarBerita)
            repository.insert(beritaAcara)
            fetchBeritaAcaraList()
            Log.d("GerejaDetailViewModel", "Inserted berita acara with id: $id")
        }
    }

    fun update(id: String, judul: String, namaGereja: String, pembicara: String, jadwalIbadah: String, deskripsi: String, gambarBerita: String) {
        viewModelScope.launch {
            val beritaAcara = BeritaAcara(id, judul, namaGereja, pembicara, jadwalIbadah, deskripsi, gambarBerita)
            repository.update(beritaAcara)
            fetchBeritaAcaraList()
            _editedBerita.value = beritaAcara
            Log.d("BeritaAcaraDetailViewModel", "Updated berita acara with id: $id")
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            repository.delete(id)
            fetchBeritaAcaraList()
            Log.d("BeritaAcaraDetailViewModel", "Deleted berita acara with id: $id")
        }
    }

    suspend fun getBeritaAcaraById(id: String): BeritaAcara? {
        Log.d("BeritaAcaraDetailViewModel", "Fetching berita acara with id: $id")
        return repository.getBeritaAcaraById(id)
    }
}