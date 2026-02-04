package dev.androhit.crosschat.auth.ui

data class AuthUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val generalError: String? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
)
