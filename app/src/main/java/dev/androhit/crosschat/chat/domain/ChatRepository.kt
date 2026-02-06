package dev.androhit.crosschat.chat.domain

import dev.androhit.crosschat.chat.domain.model.Chat
import dev.androhit.crosschat.chat.domain.model.Message
import dev.androhit.crosschat.domain.model.DataError
import dev.androhit.crosschat.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getAllChats(userId: Int): Result<List<Chat>, DataError.Network>
    suspend fun createChat(participantEmail: String): Result<Chat, DataError.Network>
    suspend fun getAllMessages(chatId: Int): Result<List<Message>, DataError.Network>
    
    suspend fun connectToSocket()
    fun disconnectFromSocket()
    fun sendMessage(chatId: Int, text: String)
    fun observeMessages(chatId: Int): Flow<Message>
}
