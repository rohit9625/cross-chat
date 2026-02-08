package dev.androhit.crosschat.chat.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: Int,
    val displayName: String,
    val lastMessageSender: String?,
    val lastMessageText: String?,
    val lastMessageTime: Long?
)
