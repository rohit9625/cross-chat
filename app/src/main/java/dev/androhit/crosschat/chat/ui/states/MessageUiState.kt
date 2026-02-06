package dev.androhit.crosschat.chat.ui.states

import dev.androhit.crosschat.chat.domain.model.Message
import dev.androhit.crosschat.util.DateTimeUtils

data class MessageUiState(
    val text: String,
    val sender: String,
    val isFromMe: Boolean,
    val timestamp: String? = null,
)

fun Message.toUiState(currentUserId: Int?) = MessageUiState(
    text = text,
    sender = senderName.split(" ").first(),
    isFromMe = senderId == currentUserId,
    timestamp = DateTimeUtils.formatTime(timestamp),
)
