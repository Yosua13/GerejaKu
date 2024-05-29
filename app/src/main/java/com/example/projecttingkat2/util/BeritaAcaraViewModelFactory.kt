package com.example.projecttingkat2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecttingkat2.firebase.BeritaAcaraRepository
import com.example.projecttingkat2.viewmodel.BeritaAcaraDetailViewModel

class BeritaAcaraViewModelFactory(private val repository: BeritaAcaraRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BeritaAcaraDetailViewModel::class.java)) {
            return BeritaAcaraDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}