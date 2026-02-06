package dev.androhit.crosschat.chat.domain.model

data class Message(
    val text: String,
    val senderId: Int,
    val senderName: String,
)
