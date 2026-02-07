package dev.androhit.crosschat.chat.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatListDto(
    val chats: List<ChatDto>
)
