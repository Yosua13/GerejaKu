package com.example.projecttingkat2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecttingkat2.firebase.PenggunaBukuRepository
import com.example.projecttingkat2.firebase.PenggunaGerejaRepository
import com.example.projecttingkat2.viewmodel.PenggunaBukuViewModel
import com.example.projecttingkat2.viewmodel.PenggunaGerejaViewModel

class PenggunaBukuViewModelFactory(private val repository: PenggunaBukuRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PenggunaBukuViewModel::class.java)) {
            return PenggunaBukuViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}