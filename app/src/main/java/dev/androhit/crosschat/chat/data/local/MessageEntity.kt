package dev.androhit.crosschat.chat.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
)
