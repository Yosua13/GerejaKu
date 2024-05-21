package com.example.projecttingkat2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttingkat2.database.GerejaDao
import com.example.projecttingkat2.model.Gereja
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class   GerejaViewModel(dao: GerejaDao) : ViewModel() {

    val data: StateFlow<List<Gereja>> = dao.getGereja().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}