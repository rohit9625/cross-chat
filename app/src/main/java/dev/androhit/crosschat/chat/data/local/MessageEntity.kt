package dev.androhit.crosschat.chat.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.androhit.crosschat.chat.domain.model.Message

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["chatId"])]
)
data class MessageEntity(
    @PrimaryKey val id: Int,
    val chatId: Int,
    val text: String,
    val senderId: Int,
    val senderName: String,
    val autoTranslate: Boolean,
    val translationStatus: String,
    val createdAt: Long,
    val updatedAt: Long,
    val translatedText: String? = null,
    val translatedLanguage: String? = null,
)

fun MessageEntity.toMessage() = Message(
    id = id,
    text = text,
    senderId = senderId,
    senderName = senderName,
    autoTranslate = autoTranslate,
    translationStatus = translationStatus,
    translatedText = translatedText,
    translatedLanguage = translatedLanguage,
    timestamp = createdAt,
)

fun Message.toEntity(chatId: Int) = MessageEntity(
    id = id,
    chatId = chatId,
    text = text,
    senderId = senderId,
    senderName = senderName,
    autoTranslate = autoTranslate,
    translationStatus = translationStatus ?: "",
    translatedText = translatedText,
    translatedLanguage = translatedLanguage,
    createdAt = timestamp ?: 0L,
    updatedAt = timestamp ?: 0L,
)
