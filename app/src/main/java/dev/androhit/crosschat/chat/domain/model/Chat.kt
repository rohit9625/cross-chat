package dev.androhit.crosschat.chat.domain.model

data class Chat(
    val id: Int,
    val displayName: String,
    val lastMessageSender: String?,
    val lastMessageText: String?,
    val lastMessageTime: Long?,
)