package com.example.projecttingkat2.navigation

import com.example.projecttingkat2.ui.screen.detail.KEY_ID_GEREJA

sealed class Screen(val route: String) {

    data object LoginRegister: Screen("loginRegisterScreen")
    data object Home: Screen("homeScreen")
    data object GerejaFormBaru: Screen("gerejaDetailScreen")
    data object GerejaFormUbah: Screen("gerejaDetailScreen/{$KEY_ID_GEREJA}") {
        fun gerejaId(id: Long) = "gerejaDetailScreen/$id"
    }
    data object Book: Screen("bookScreen")
    data object BeritaAcara: Screen("beritaAcaraScreen")
    data object Profil: Screen("profilScreen")
}