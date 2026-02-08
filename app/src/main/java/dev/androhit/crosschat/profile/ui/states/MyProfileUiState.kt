package dev.androhit.crosschat.profile.ui.states

data class MyProfileUiState(
    val name: String = "",
    val email: String = "",
    val preferredLanguage: String = "en",
    val isLoading: Boolean = false,
    val error: String? = null
)
