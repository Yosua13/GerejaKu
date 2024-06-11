package com.example.projecttingkat2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecttingkat2.firebase.PenggunaGerejaRepository
import com.example.projecttingkat2.viewmodel.PenggunaGerejaViewModel

class PenggunaGerejaViewModelFactory(private val repository: PenggunaGerejaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PenggunaGerejaViewModel::class.java)) {
            return PenggunaGerejaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}