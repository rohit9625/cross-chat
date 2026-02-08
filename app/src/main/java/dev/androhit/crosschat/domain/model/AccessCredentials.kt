package dev.androhit.crosschat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AccessCredentials(
    val userId: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val preferredLanguage: String = "en"
)
