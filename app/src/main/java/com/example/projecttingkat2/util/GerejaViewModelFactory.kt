package com.example.projecttingkat2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecttingkat2.firebase.GerejaRepository
import com.example.projecttingkat2.viewmodel.GerejaDetailViewModel

class GerejaViewModelFactory(private val repository: GerejaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GerejaDetailViewModel::class.java)) {
            return GerejaDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}