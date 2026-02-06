package dev.androhit.crosschat.chat.domain

import dev.androhit.crosschat.chat.domain.model.Chat
import dev.androhit.crosschat.domain.model.DataError
import dev.androhit.crosschat.domain.model.Result

interface ChatRepository {
    suspend fun getAllChats(userId: Int):Result<List<Chat>, DataError.Network>
    suspend fun createChat(participantEmail: String): Result<Chat, DataError.Network>
}