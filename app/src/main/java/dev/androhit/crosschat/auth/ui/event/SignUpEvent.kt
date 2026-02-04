package dev.androhit.crosschat.auth.ui.event

sealed interface SignUpEvent {
    data class OnNameChanged(val name: String): SignUpEvent
    data class OnEmailChanged(val email: String): SignUpEvent
    data class OnPasswordChanged(val password: String): SignUpEvent
    data class OnSubmit(val onSuccess: () -> Unit): SignUpEvent
}