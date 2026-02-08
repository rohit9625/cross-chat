package dev.androhit.crosschat.chat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androhit.crosschat.chat.domain.ChatRepository
import dev.androhit.crosschat.chat.ui.event.MessagingEvent
import dev.androhit.crosschat.chat.ui.states.MessagingUiState
import dev.androhit.crosschat.chat.ui.states.toUiState
import dev.androhit.crosschat.data.CredentialManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MessagingViewModel(
    private val chatId: Int,
    private val chatTitle: String,
    private val repository: ChatRepository,
    private val credentialManager: CredentialManager
) : ViewModel() {

    private val _message = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState = flow {
        val currentUserId = credentialManager.getAccessCredentials().userId
        emit(currentUserId)
    }.flatMapLatest { userId ->
        combine(
            repository.getMessagesForChat(chatId),
            _message,
            _isLoading,
            _error
        ) { messages, message, isLoading, error ->
            MessagingUiState(
                chatHistory = messages.map { it.toUiState(userId) },
                message = message,
                chatTitle = chatTitle,
                isLoadingNewMessages = isLoading,
                error = error
            )
        }.onStart {
            refreshMessages()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MessagingUiState(chatTitle = chatTitle, isLoadingNewMessages = true),
    )

    init {
        observeMessages()
    }

    private fun refreshMessages() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.refreshMessages(chatId)
            _isLoading.value = false
        }
    }

    private fun observeMessages() {
        viewModelScope.launch {
            repository.connectToSocket()
            repository.observeMessages(chatId).launchIn(viewModelScope)
        }
    }

    fun onEvent(event: MessagingEvent) {
        when (event) {
            is MessagingEvent.OnMessageChanged -> {
                _message.value = event.message
            }
            MessagingEvent.OnSendMessage -> {
                val currentMessage = _message.value
                if (currentMessage.isNotBlank()) {
                    repository.sendMessage(chatId, currentMessage)
                    _message.value = ""
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.disconnectFromSocket()
    }
}
