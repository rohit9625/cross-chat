package dev.androhit.crosschat.auth.ui.event

sealed interface SignInEvent {
    data class OnEmailChanged(val email: String): SignInEvent
    data class OnPasswordChanged(val password: String): SignInEvent
    data class OnSubmit(val onSuccess: () -> Unit): SignInEvent
}