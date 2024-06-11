package com.example.projecttingkat2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttingkat2.firebase.PenggunaBukuRepository
import com.example.projecttingkat2.firebase.PenggunaGerejaRepository
import com.example.projecttingkat2.model.Buku
import com.example.projecttingkat2.model.Gereja
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PenggunaBukuViewModel(private val repository: PenggunaBukuRepository) : ViewModel() {
    private val _buku = MutableStateFlow<Buku?>(null)
    val buku: StateFlow<Buku?> get() = _buku

    fun getBukuById(id: String) {
        viewModelScope.launch {
            Log.d("BukuDetailViewModel", "Fetching Buku with ID: $id")
            val result = repository.getBukuById(id)
            if (result != null) {
                Log.d("BukuDetailViewModel", "Buku data retrieved: $result")
            } else {
                Log.d("BukuDetailViewModel", "Buku data not found for ID: $id")
            }
            _buku.value = result
        }
    }
}
