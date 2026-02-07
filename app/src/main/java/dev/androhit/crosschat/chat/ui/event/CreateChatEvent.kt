package dev.androhit.crosschat.chat.ui.event

import dev.androhit.crosschat.chat.domain.model.User

sealed interface CreateChatEvent {
    data class OnSearchQueryChange(val query: String): CreateChatEvent
    data class OnUserSelected(val user: User): CreateChatEvent
    data class OnCreateChat(val onSuccess: (chatId: Int, chatTitle: String) -> Unit): CreateChatEvent
}