package dev.androhit.crosschat.chat.domain

import dev.androhit.crosschat.chat.domain.model.Chat
import dev.androhit.crosschat.chat.domain.model.Message
import dev.androhit.crosschat.chat.domain.model.User
import dev.androhit.crosschat.domain.model.DataError
import dev.androhit.crosschat.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getAllChats(userId: Int): Flow<List<Chat>>
    fun getMessagesForChat(chatId: Int): Flow<List<Message>>
    suspend fun createChat(participantEmail: String): Result<Chat, DataError.Network>
    suspend fun searchUsersByEmail(email: String): Result<List<User>, DataError.Network>
    suspend fun refreshChats(userId: Int): Result<Unit, DataError.Network>
    suspend fun refreshMessages(chatId: Int): Result<Unit, DataError.Network>
    
    suspend fun connectToSocket()
    fun disconnectFromSocket()
    fun sendMessage(chatId: Int, text: String)
    suspend fun observeMessages(chatId: Int)
}
