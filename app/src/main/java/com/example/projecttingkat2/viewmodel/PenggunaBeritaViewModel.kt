package com.example.projecttingkat2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttingkat2.firebase.PenggunaBeritaRepository
import com.example.projecttingkat2.firebase.PenggunaGerejaRepository
import com.example.projecttingkat2.model.BeritaAcara
import com.example.projecttingkat2.model.Gereja
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PenggunaBeritaViewModel(private val repository: PenggunaBeritaRepository) : ViewModel() {
    private val _berita = MutableStateFlow<BeritaAcara?>(null)
    val berita: StateFlow<BeritaAcara?> get() = _berita

    fun getBeritaById(id: String) {
        viewModelScope.launch {
            Log.d("BeritaDetailViewModel", "Fetching Berita with ID: $id")
            val result = repository.getBeritaById(id)
            if (result != null) {
                Log.d("BeritaDetailViewModel", "Berita data retrieved: $result")
            } else {
                Log.d("BeritaDetailViewModel", "Berita data not found for ID: $id")
            }
            _berita.value = result
        }
    }
}
