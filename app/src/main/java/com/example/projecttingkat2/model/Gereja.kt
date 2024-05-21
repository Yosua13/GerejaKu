package com.example.projecttingkat2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gereja")
data class Gereja(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val judul: String,
    val aliran: String,
    val lokasi: String,
    val link: String,
    val jadwal: String,
    val deskripsi: String,
    val gambar: String
)