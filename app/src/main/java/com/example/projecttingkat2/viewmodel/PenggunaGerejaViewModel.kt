package com.example.projecttingkat2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttingkat2.firebase.PenggunaGerejaRepository
import com.example.projecttingkat2.model.Gereja
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PenggunaGerejaViewModel(private val repository: PenggunaGerejaRepository) : ViewModel() {
    private val _gereja = MutableStateFlow<Gereja?>(null)
    val gereja: StateFlow<Gereja?> get() = _gereja

    fun getGerejaById(id: String) {
        viewModelScope.launch {
            Log.d("GerejaDetailViewModel", "Fetching Gereja with ID: $id")
            val result = repository.getGerejaById(id)
            if (result != null) {
                Log.d("GerejaDetailViewModel", "Gereja data retrieved: $result")
            } else {
                Log.d("GerejaDetailViewModel", "Gereja data not found for ID: $id")
            }
            _gereja.value = result
        }
    }
}
