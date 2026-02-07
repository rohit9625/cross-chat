package dev.androhit.crosschat.chat.ui.states

import dev.androhit.crosschat.chat.domain.model.Chat
import dev.androhit.crosschat.util.DateTimeUtils

data class ChatUiState(
    val id: Int,
    val title: String,
    val subTitle: String,
    val lastMessageTime: String?,
)

fun Chat.toUiState() = ChatUiState(
    id = id,
    title = displayName,
    subTitle = lastMessage?.let {
        "${lastMessage.senderName?.split(" ")?.first()}: ${lastMessage.text}"
    } ?: "Your chat with $displayName",
    lastMessageTime = DateTimeUtils.formatTime(lastMessageTime),
)
