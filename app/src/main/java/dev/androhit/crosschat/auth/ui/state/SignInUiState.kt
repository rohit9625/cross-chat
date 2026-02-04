package dev.androhit.crosschat.auth.ui.state

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val generalError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
)