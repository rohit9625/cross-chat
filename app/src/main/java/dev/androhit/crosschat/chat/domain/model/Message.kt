package dev.androhit.crosschat.chat.domain.model

import java.util.Date

data class Message(
    val id: Int,
    val text: String,
    val senderId: Int,
    val senderName: String,
    val timestamp: Date? = null,
)
