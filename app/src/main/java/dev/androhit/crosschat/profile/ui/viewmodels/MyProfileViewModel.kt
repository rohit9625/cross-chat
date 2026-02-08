package dev.androhit.crosschat.profile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androhit.crosschat.data.CredentialManager
import dev.androhit.crosschat.profile.domain.UserRepository
import dev.androhit.crosschat.profile.ui.states.MyProfileUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MyProfileViewModel(
    private val credentialManager: CredentialManager,
    private val userRepository: UserRepository
) : ViewModel() {

    val uiState = credentialManager.credentialsFlow
        .map { credentials ->
            MyProfileUiState(
                name = credentials.name  ?: "",
                email = credentials.email ?: "",
                preferredLanguage = credentials.preferredLanguage
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MyProfileUiState()
        )

    fun updateLanguage(language: String) {
        viewModelScope.launch {
            userRepository.updatePreferredLanguage(language)
        }
    }
}
