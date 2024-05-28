package com.example.projecttingkat2.model


data class User(
    val id: Long = 0L,
    val name: String,
    val email: String,
    val nomorHp: String,
    val tanggal: String,
    val password: String
)