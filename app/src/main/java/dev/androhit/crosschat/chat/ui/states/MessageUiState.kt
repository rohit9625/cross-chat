package dev.androhit.crosschat.chat.ui.states

data class MessageUiState(
    val text: String,
    val sender: String,
    val isFromMe: Boolean,
    val timestamp: String? = null,
)
