package dev.androhit.crosschat.profile.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LanguageUpdateRequest(
    @SerialName("preferred_language")
    val preferredLanguage: String
)
