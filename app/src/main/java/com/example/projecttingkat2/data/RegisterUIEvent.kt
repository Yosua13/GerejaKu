package com.example.projecttingkat2.data

sealed class RegisterUIEvent {
    data class FirstNameChanged(val firstName: String) : RegisterUIEvent()
    data class LastNameChanged(val lastName: String) : RegisterUIEvent()
    data class EmailChanged(val email: String) : RegisterUIEvent()
    data class PasswordChanged(val password: String) : RegisterUIEvent()

    object RegisterButtonClicked : RegisterUIEvent()
}