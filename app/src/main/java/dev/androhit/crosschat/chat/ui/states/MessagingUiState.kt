package dev.androhit.crosschat.chat.ui.states

data class MessagingUiState(
    val chatHistory: List<MessageUiState> = emptyList(),
    val message: String = "",
    val chatTitle: String = "",
)

data class MessageUiState(
    val text: String,
    val sender: String,
    val isFromMe: Boolean,
    val timestamp: String? = null,
)
