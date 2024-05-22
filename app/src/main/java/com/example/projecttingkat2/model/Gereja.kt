package com.example.projecttingkat2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Gereja(
    val id: Long = 0L,
    val judul: String,
    val aliran: String,
    val lokasi: String,
    val link: String,
    val jadwal: String,
    val deskripsi: String,
    val gambar: String
)