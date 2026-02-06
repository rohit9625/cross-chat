package dev.androhit.crosschat.chat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androhit.crosschat.chat.domain.ChatRepository
import dev.androhit.crosschat.chat.ui.event.MessagingEvent
import dev.androhit.crosschat.chat.ui.states.MessageUiState
import dev.androhit.crosschat.chat.ui.states.MessagingUiState
import dev.androhit.crosschat.chat.ui.states.toUiState
import dev.androhit.crosschat.data.CredentialManager
import dev.androhit.crosschat.util.DateTimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessagingViewModel(
    private val chatId: Int,
    private val chatTitle: String,
    private val repository: ChatRepository,
    private val credentialManager: CredentialManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessagingUiState(chatTitle = chatTitle))
    val uiState = _uiState.asStateFlow()

    init {
        observeMessages()
    }

    private fun observeMessages() {
        viewModelScope.launch {
            val currentUserId = credentialManager.getAccessCredentials().userId
            repository.connectToSocket()
            repository.observeMessages(chatId).collect { message ->
                val newMessage = message.toUiState(currentUserId).copy(
                    sender = message.senderName ?: chatTitle.split("").first(),
                )
                _uiState.update {
                    it.copy(chatHistory = listOf(newMessage) + it.chatHistory)
                }
            }
        }
    }

    fun onEvent(event: MessagingEvent) {
        when (event) {
            is MessagingEvent.OnMessageChanged -> {
                _uiState.update { it.copy(message = event.message) }
            }
            MessagingEvent.OnSendMessage -> {
                val currentMessage = _uiState.value.message
                if (currentMessage.isNotBlank()) {
                    repository.sendMessage(chatId, currentMessage)
                    _uiState.update { it.copy(message = "") }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.disconnectFromSocket()
    }
}
