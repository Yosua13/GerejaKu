package com.example.a3uichatai

import android.graphics.Bitmap
import com.example.a3uichatai.data.Chat

data class ChatState(
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null
    )
