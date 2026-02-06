package dev.androhit.crosschat.chat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androhit.crosschat.chat.domain.ChatRepository
import dev.androhit.crosschat.chat.ui.event.MessagingEvent
import dev.androhit.crosschat.chat.ui.states.MessagingUiState
import dev.androhit.crosschat.chat.ui.states.toUiState
import dev.androhit.crosschat.data.CredentialManager
import dev.androhit.crosschat.domain.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessagingViewModel(
    private val chatId: Int,
    private val chatTitle: String,
    private val repository: ChatRepository,
    private val credentialManager: CredentialManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessagingUiState(chatTitle = chatTitle))
    val uiState = _uiState.onStart {
        fetchChatMessages()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MessagingUiState(chatTitle = chatTitle),
    )

    init {
        observeMessages()
    }

    private suspend fun fetchChatMessages() {
        val currentUserId = credentialManager.getAccessCredentials().userId
        when(val result = repository.getAllMessages(chatId)) {
            is Result.Success -> {
                val messages = result.data.map { msg ->
                    msg.toUiState(currentUserId).copy(
                        sender = msg.senderName ?: chatTitle.split("").first(),
                    )
                }
                _uiState.update { it.copy(chatHistory = messages) }
            }
            is Result.Error -> {
                _uiState.update { it.copy(error = result.error.toString()) }
            }
        }
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
