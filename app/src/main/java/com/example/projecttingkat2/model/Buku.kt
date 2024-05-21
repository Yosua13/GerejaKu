package com.example.projecttingkat2.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Buku(
    @DrawableRes val buku: Int,
    @StringRes val judul: Int,
    @StringRes val penulis: Int,
    @StringRes val sinopsis: Int,
)