package dev.androhit.crosschat.chat.ui.states

data class ChatListUiState(
    val isLoading: Boolean = false,
    val chats: List<ChatUiState> = emptyList(),
    val error: String? = null,
)