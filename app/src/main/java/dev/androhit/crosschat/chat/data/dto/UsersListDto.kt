package dev.androhit.crosschat.chat.data.dto

import dev.androhit.crosschat.auth.data.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UsersListDto(
    val users: List<User>
)
