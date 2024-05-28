package com.example.projecttingkat2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecttingkat2.firebase.BukuRepository
import com.example.projecttingkat2.viewmodel.BukuDetailViewModel

class BukuViewModelFactory(private val repository: BukuRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BukuDetailViewModel::class.java)) {
            return BukuDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}