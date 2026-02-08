package dev.androhit.crosschat.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    @SerialName("preferred_language")
    val preferredLanguage: String,
)