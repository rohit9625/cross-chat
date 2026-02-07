package dev.androhit.crosschat.chat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androhit.crosschat.chat.domain.use_case.GetAllChatsUseCase
import dev.androhit.crosschat.chat.ui.states.ChatListUiState
import dev.androhit.crosschat.chat.ui.states.toUiState
import dev.androhit.crosschat.domain.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val getAllChatsUseCase: GetAllChatsUseCase,
): ViewModel() {
    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when(val result = getAllChatsUseCase.invoke()) {
                is Result.Success -> {
                    val chats = result.data.map { it.toUiState() }
                    _uiState.update { it.copy(isLoading = false, chats = chats) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.error.toString()) }
                }
            }
        }
    }
}