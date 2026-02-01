package dev.androhit.crosschat.auth.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(e: AuthEvent) {
        when(e) {
            is AuthEvent.OnEmailChanged -> {
                _uiState.update { it.copy(email = e.email) }
            }
            is AuthEvent.OnNameChanged -> {
                _uiState.update { it.copy(name = e.name) }
            }
            is AuthEvent.OnPasswordChanged -> {
                _uiState.update { it.copy(password = e.password) }
            }
            is AuthEvent.OnSubmit -> {
                when(e.action) {
                    SubmitAction.SIGN_IN -> TODO("Sign In existing user")
                    SubmitAction.SIGN_UP -> TODO("Create a new account")
                }
            }
        }
    }
}