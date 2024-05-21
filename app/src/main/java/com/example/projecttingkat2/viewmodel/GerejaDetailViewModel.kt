package com.example.projecttingkat2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttingkat2.database.GerejaDao
import com.example.projecttingkat2.model.Gereja
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GerejaDetailViewModel(private val dao: GerejaDao) : ViewModel() {

    fun insert(judul: String, aliran: String, lokasi: String, link: String, jadwal: String, deskripsi: String, gambar: String) {
        val gereja = Gereja(
        judul = judul,
        aliran = aliran,
        lokasi = lokasi,
        link = link,
        jadwal = jadwal,
        deskripsi = deskripsi,
        gambar = gambar
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(gereja)
        }
    }
    suspend fun getGereja(id: Long): Gereja? {
        return dao.getGerejaById(id)
    }

    fun update(id: Long, judul: String, aliran: String, lokasi: String, link: String, jadwal: String, deskripsi: String, gambar: String) {
        val gereja = Gereja(
            id = id,
            judul = judul,
            aliran = aliran,
            lokasi = lokasi,
            link = link,
            jadwal = jadwal,
            deskripsi = deskripsi,
            gambar = gambar
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(gereja)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}