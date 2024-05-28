package com.example.projecttingkat2.navigation

import com.example.projecttingkat2.ui.screen.detail.KEY_ID_BUKU
import com.example.projecttingkat2.ui.screen.detail.KEY_ID_GEREJA

sealed class Screen(val route: String) {

    data object LoginRegister: Screen("loginRegisterScreen")
    data object Home: Screen("homeScreen")
    data object GerejaFormBaru: Screen("gerejaDetailScreen")
    data object GerejaFormUbah: Screen("gerejaDetailScreen/{$KEY_ID_GEREJA}") {
        fun gerejaId(id: String) = "gerejaDetailScreen/$id"
    }
    data object Book: Screen("bookScreen")
    data object BukuFormBaru: Screen("bukuDetailScreen")
    data object BukuFormUbah: Screen("bukuDetailScreen/{$KEY_ID_BUKU}") {
        fun bukuId(id: String) = "bukuDetailScreen/$id"
    }
    data object BeritaAcara: Screen("beritaAcaraScreen")
    data object Profil: Screen("profilScreen")
}