package dev.androhit.crosschat.chat.data.dto

import dev.androhit.crosschat.chat.data.local.MessageEntity
import dev.androhit.crosschat.util.DateTimeUtils
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: Int,
    @SerialName("chat_id")
    val chatId: Int,
    @SerialName("sender_id")
    val senderId: Int,
    @SerialName("sender_name")
    val senderName: String,
    val content: String,
    @SerialName("auto_translate")
    val autoTranslate: Boolean,
    @SerialName("translation_status")
    val translationStatus: String,
    @SerialName("translated_text")
    val translatedText: String? = null,
    @SerialName("translated_language")
    val translatedLanguage: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
)

fun MessageDto.asEntity() = MessageEntity(
    id = id,
    chatId = chatId,
    text = content,
    senderId = senderId,
    senderName = senderName,
    autoTranslate = autoTranslate,
    translationStatus = translationStatus,
    translatedText = translatedText,
    createdAt = DateTimeUtils.parseUtcDateToLong(createdAt) ?: 0L,
    updatedAt = DateTimeUtils.parseUtcDateToLong(updatedAt) ?: 0L,
)
