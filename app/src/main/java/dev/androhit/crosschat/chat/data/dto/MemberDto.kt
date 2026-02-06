package dev.androhit.crosschat.chat.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberDto(
    @SerialName("user_id")
    val userId: Int,
    val name: String,
    val email: String,
)
