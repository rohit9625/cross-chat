package dev.androhit.crosschat.chat.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageListDto(
    val messages: List<MessageDto>
)
