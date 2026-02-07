package dev.androhit.crosschat.chat.ui.states

import dev.androhit.crosschat.chat.domain.model.User

data class CreateChatUiState(
    val searchQuery: String = "",
    val searchedUsers: List<User> = emptyList(),
    val selectedUser: User? = null,
    val isLoading: Boolean = false,
)
