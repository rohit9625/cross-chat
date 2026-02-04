package dev.androhit.crosschat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AccessCredentials(
    val refreshToken: String? = null,
    val accessToken: String? = null
)