package com.example.projecttingkat2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecttingkat2.firebase.ProfilRepository
import com.example.projecttingkat2.viewmodel.ProfileDetailViewModel

class ProfileViewModelFactory(private val repository: ProfilRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}