package dev.androhit.crosschat.chat.ui.states

data class MessagingUiState(
    val chatHistory: List<MessageUiState> = emptyList(),
    val message: String = "",
    val chatTitle: String = "",
    val isLoadingNewMessages: Boolean = false,
    val error: String? = null,
)

