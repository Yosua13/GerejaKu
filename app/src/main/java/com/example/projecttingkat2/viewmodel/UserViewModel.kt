package com.example.projecttingkat2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttingkat2.firebase.UserRepository
import com.example.projecttingkat2.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess

    private val _registrationError = MutableStateFlow<String?>(null)
    val registrationError: StateFlow<String?> = _registrationError

    fun registerUser(user: User) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        user.uid = firebaseUser?.uid ?: ""
                        saveUserToFirestore(user)
                    } else {
                        _registrationError.value = task.exception?.message
                    }
                }
        }
    }

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loginSuccess.value = true
                    } else {
                        val errorMessage = when (val exception = task.exception) {
                            is FirebaseAuthException -> when (exception.errorCode) {
                                "ERROR_INVALID_EMAIL" -> "Email tidak valid."
                                "ERROR_USER_NOT_FOUND" -> "Email tidak ditemukan."
                                "ERROR_WRONG_PASSWORD" -> "Password salah."
                                "ERROR_USER_DISABLED" -> "Akun dinonaktifkan."
                                "ERROR_TOO_MANY_REQUESTS" -> "Terlalu banyak percobaan. Coba lagi nanti."
                                else -> "Kesalahan autentikasi."
                            }
                            else -> exception?.message ?: "Kesalahan tidak diketahui."
                        }
                        _loginError.value = errorMessage
                    }
                }
        }
    }

    // Update user profile
    private val userRepository = UserRepository()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> get() = _updateSuccess

    private val _updateError = MutableStateFlow<String?>(null)
    val updateError: StateFlow<String?> get() = _updateError

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        val userId = userRepository.auth.currentUser?.uid
        userId?.let {
            fetchUserData(it)
        }
    }

    fun fetchUserData(userId: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserById(userId)
                _currentUser.value = user
            } catch (e: Exception) {
                _updateError.value = e.message
                Log.d("UserViewModel", "Error fetching user data: ${e.message}")
            }
        }
    }

    fun updateProfile(firstName: String, lastName: String, newPassword: String?, email: String, nomorHP: String, tanggal: String, role: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userId = _currentUser.value?.uid ?: return
        val userDocRef = userRepository.firestore.collection("users").document(userId)

        viewModelScope.launch {
            try {
                val updates = hashMapOf<String, Any>(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "email" to email,
                    "nomorHp" to nomorHP,
                    "tanggal" to tanggal,
                    "role" to role
                )

                if (!newPassword.isNullOrBlank()) {
                    val user = userRepository.auth.currentUser
                    user?.updatePassword(newPassword)?.await()
                    updates["password"] = newPassword
                }

                userDocRef.update(updates).await()
                fetchUserData(userId) // Refresh current user data
                _updateSuccess.value = true
                _updateError.value = null
                Log.d("UserViewModel", "User profile updated successfully.")
                onSuccess()
            } catch (e: Exception) {
                _updateError.value = e.message
                _updateSuccess.value = false
                Log.d("UserViewModel", "Error updating user profile: ${e.message}")
                onFailure(e)
            }
        }
    }

    // Fungsi untuk logout
    fun logout() {
        // Hapus informasi pengguna dari state
        _currentUser.value = null
        // Tambahkan logika tambahan jika perlu, misalnya menghapus token dari penyimpanan
    }

    private fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.uid)
            .set(user)
            .addOnSuccessListener {
                _registrationSuccess.value = true
            }
            .addOnFailureListener { e ->
                _registrationError.value = e.message
            }
    }
}