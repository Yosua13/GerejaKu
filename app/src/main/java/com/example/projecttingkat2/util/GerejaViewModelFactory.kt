package com.example.projecttingkat2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecttingkat2.database.GerejaDao
import com.example.projecttingkat2.viewmodel.GerejaDetailViewModel
import com.example.projecttingkat2.viewmodel.GerejaViewModel
import java.lang.IllegalArgumentException

class GerejaViewModelFactory(private val dao: GerejaDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GerejaViewModel::class.java)) {
            return GerejaViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(GerejaDetailViewModel::class.java)) {
            return GerejaDetailViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}