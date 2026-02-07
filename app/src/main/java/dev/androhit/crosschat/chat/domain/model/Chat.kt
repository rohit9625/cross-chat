package dev.androhit.crosschat.chat.domain.model

import java.util.Date

data class Chat(
    val id: Int,
    val displayName: String,
    val lastMessage: Message?,
    val lastMessageTime: Date?,
)