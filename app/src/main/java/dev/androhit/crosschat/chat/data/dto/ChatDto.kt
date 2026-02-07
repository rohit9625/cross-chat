package dev.androhit.crosschat.chat.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val id: Int,
    val type: String,
    @SerialName("last_message_at")
    val lastMessageAt: String?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("last_message")
    val lastMessage: MessageDto?,
    val members: List<MemberDto>,
)
