package com.example.projecttingkat2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttingkat2.data.RegistrationUIState
import com.example.projecttingkat2.firebase.ProfilRepository
import com.example.projecttingkat2.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileDetailViewModel(private val repository: ProfilRepository) : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> get() = _currentUser

    private val _uiState = MutableStateFlow(RegistrationUIState())
    val uiState: StateFlow<RegistrationUIState> get() = _uiState

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            if (firebaseUser != null) {
                _currentUser.value = User(
                    id = firebaseUser.uid,
                    firstName = firebaseUser.displayName?.split(" ")?.first() ?: "",
                    lastName = firebaseUser.displayName?.split(" ")?.last() ?: "",
                    email = firebaseUser.email ?: ""
                )
                Log.d("ProfileDetailViewModel", "Current user fetched: ${_currentUser.value}")
            } else {
                Log.e("ProfileDetailViewModel", "FirebaseUser is null")
            }
        }
    }

    fun updateProfile(
        firstName: String,
        lastName: String,
        email: String,
        password: String?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val currentUser = _currentUser.value ?: run {
            Log.e("ProfileDetailViewModel", "Current user is null")
            return
        }
        viewModelScope.launch {
            try {
                Log.d("ProfileDetailViewModel", "Updating profile")
                repository.updateProfile(currentUser.id, firstName, lastName, email, password)
                fetchCurrentUser()
                Log.d("ProfileDetailViewModel", "Profile updated successfully")
                onSuccess()
            } catch (e: Exception) {
                Log.e("ProfileDetailViewModel", "Error updating profile: ${e.message}", e)
                onFailure(e)
            }
        }
    }

    fun updateUiState(newState: RegistrationUIState) {
        _uiState.value = newState
        Log.d("ProfileDetailViewModel", "UI state updated: $newState")
    }
}