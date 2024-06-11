package com.example.projecttingkat2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecttingkat2.firebase.PenggunaBeritaRepository
import com.example.projecttingkat2.firebase.PenggunaGerejaRepository
import com.example.projecttingkat2.viewmodel.PenggunaBeritaViewModel
import com.example.projecttingkat2.viewmodel.PenggunaGerejaViewModel

class PenggunaBeritaViewModelFactory(private val repository: PenggunaBeritaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PenggunaBeritaViewModel::class.java)) {
            return PenggunaBeritaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}