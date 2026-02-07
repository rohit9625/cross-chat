package dev.androhit.crosschat.chat.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androhit.crosschat.chat.domain.ChatRepository
import dev.androhit.crosschat.chat.ui.event.CreateChatEvent
import dev.androhit.crosschat.chat.ui.states.CreateChatUiState
import dev.androhit.crosschat.domain.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateChatViewModel(
    private val repository: ChatRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(CreateChatUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(e: CreateChatEvent) {
        when(e) {
            is CreateChatEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = e.query) }
            }
            is CreateChatEvent.OnUserSelected -> {
                _uiState.update { it.copy(selectedUser = e.user) }
            }
            is CreateChatEvent.OnCreateChat -> {
                val participantEmail = _uiState.value.selectedUser?.email ?: return
                _uiState.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    when(val result = repository.createChat(participantEmail)) {
                        is Result.Success -> {
                            e.onSuccess(result.data.id, result.data.displayName)
                        }
                        is Result.Error -> {
                            Log.e("CreateChatViewModel", "Error creating chat: ${result.error.name}")
                            /** TODO("Show error in snackbar) **/
                        }
                    }
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}