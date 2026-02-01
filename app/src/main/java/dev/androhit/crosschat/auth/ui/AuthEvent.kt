package dev.androhit.crosschat.auth.ui

sealed interface AuthEvent {
    data class OnNameChanged(val name: String): AuthEvent
    data class OnEmailChanged(val email: String): AuthEvent
    data class OnPasswordChanged(val password: String): AuthEvent
    data class OnSubmit(val action: SubmitAction, val onSuccess: () -> Unit): AuthEvent
}

enum class SubmitAction {
    SIGN_IN,
    SIGN_UP,
}