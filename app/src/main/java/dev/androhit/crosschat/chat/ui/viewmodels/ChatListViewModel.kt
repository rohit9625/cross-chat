package dev.androhit.crosschat.chat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androhit.crosschat.chat.domain.ChatRepository
import dev.androhit.crosschat.chat.ui.states.ChatListUiState
import dev.androhit.crosschat.chat.ui.states.toUiState
import dev.androhit.crosschat.data.CredentialManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ChatListViewModel(
    private val repository: ChatRepository,
    private val credentialManager: CredentialManager,
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState = flow {
        val userId = credentialManager.getAccessCredentials().userId
        emit(userId)
    }.flatMapLatest { userId ->
        if (userId == null) {
            flow { emit(ChatListUiState(error = "Unauthorized")) }
        } else {
            combine(
                repository.getAllChats(userId),
                _isLoading,
                _error
            ) { chats, isLoading, error ->
                ChatListUiState(
                    chats = chats.map { it.toUiState() },
                    isLoading = isLoading,
                    error = error
                )
            }.onStart {
                refreshChats(userId)
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ChatListUiState(isLoading = true)
    )

    private fun refreshChats(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.refreshChats(userId)
            _isLoading.value = false
        }
    }
}
