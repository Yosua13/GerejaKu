package com.example.projecttingkat2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val email: String,
    val nomorHp: String,
    val tanggal: String,
    val password: String
)