package dev.androhit.crosschat.chat.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androhit.crosschat.chat.domain.ChatRepository
import dev.androhit.crosschat.chat.ui.event.CreateChatEvent
import dev.androhit.crosschat.chat.ui.states.CreateChatUiState
import dev.androhit.crosschat.domain.model.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CreateChatViewModel(
    private val repository: ChatRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(CreateChatUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        _searchQuery
            .debounce(500L)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isBlank()) {
                    _uiState.update { it.copy(searchedUsers = emptyList(), selectedUser = null) }
                }
            }
            .flatMapLatest { query ->
                if (query.length >= 3) {
                    val result = repository.searchUsersByEmail(query)
                    flowOf(result)
                } else {
                    emptyFlow()
                }
            }
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { it.copy(searchedUsers = result.data) }
                    }
                    is Result.Error -> {
                        Log.e("CreateChatViewModel", "Error searching users: ${result.error.name}")
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(e: CreateChatEvent) {
        when(e) {
            is CreateChatEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = e.query) }
                _searchQuery.value = e.query
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
