package dev.androhit.crosschat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val code: String,
    val message: String,
)
