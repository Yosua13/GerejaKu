package com.example.projecttingkat2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projecttingkat2.model.Gereja
import kotlinx.coroutines.flow.Flow

@Dao
interface GerejaDao {
    @Insert
    suspend fun insert(gereja: Gereja)

    @Update
    suspend fun update(gereja: Gereja)

    @Query("SELECT * FROM gereja ORDER BY id DESC")
    fun getGereja(): Flow<List<Gereja>>

    @Query("SELECT * FROM gereja WHERE id = :id")
    suspend fun getGerejaById(id: Long): Gereja?

    @Query("DELETE FROM gereja WHERE id = :id")
    suspend fun deleteById(id: Long)
}