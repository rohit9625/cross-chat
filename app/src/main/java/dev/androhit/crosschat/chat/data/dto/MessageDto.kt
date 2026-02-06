package dev.androhit.crosschat.chat.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: Int,
    @SerialName("chat_id")
    val chatId: Int,
    @SerialName("sender_id")
    val senderId: Int,
    val content: String,
    @SerialName("created_at")
    val createdAt: String,
)