package com.example.projecttingkat2.viewmodel

import com.example.projecttingkat2.R
import com.example.projecttingkat2.model.Buku

object BookViewModel {
    val dataBuku = listOf(
        Buku(
            buku = R.drawable.the_justification_of_god,
            judul = R.string.judul1,
            penulis = R.string.john_piper,
            sinopsis = R.string.sinopsis1
        ),
        Buku(
            buku = R.drawable.the_legacy_of_sovereign_joy,
            judul = R.string.judul2,
            penulis = R.string.john_piper,
            sinopsis = R.string.sinopsis2
        ),
        Buku(
            buku = R.drawable.the_pleasures_of_god,
            judul = R.string.judul3,
            penulis = R.string.john_piper,
            sinopsis = R.string.sinopsis3
        )
        ,Buku(
            buku = R.drawable.the_hidden_smile_of_god,
            judul = R.string.judul4,
            penulis = R.string.john_piper,
            sinopsis = R.string.sinopsis4
        ),
        Buku(
            buku = R.drawable.the_dangerous_duty_of_delight,
            judul = R.string.judul5,
            penulis = R.string.john_piper,
            sinopsis = R.string.sinopsis5
        )
        ,Buku(
             buku = R.drawable.the_misery_of_job_and_the_mercy_of_god,
            judul = R.string.judul8,
            penulis = R.string.john_piper,
            sinopsis = R.string.sinopsis8
        )
    )
}