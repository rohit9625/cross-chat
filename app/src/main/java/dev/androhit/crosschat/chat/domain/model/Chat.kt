package dev.androhit.crosschat.chat.domain.model

data class Chat(
    val id: Int,
    val displayName: String,
    val lastMessage: Message?,
    val lastMessageTime: Long?,
)