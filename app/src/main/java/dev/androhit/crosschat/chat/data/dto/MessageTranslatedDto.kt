package dev.androhit.crosschat.chat.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageTranslatedDto(
    @SerialName("message_id")
    val messageId: String,
    val language: String,
    @SerialName("translated_text")
    val translatedText: String
)
