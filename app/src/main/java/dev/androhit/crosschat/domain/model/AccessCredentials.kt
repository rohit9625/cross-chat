package dev.androhit.crosschat.domain.model

data class AccessCredentials(
    val refreshToken: String? = null,
    val accessToken: String? = null
)