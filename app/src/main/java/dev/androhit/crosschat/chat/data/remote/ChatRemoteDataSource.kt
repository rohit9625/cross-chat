package dev.androhit.crosschat.chat.data.remote

import dev.androhit.crosschat.chat.data.dto.ChatDto
import dev.androhit.crosschat.chat.data.dto.ChatListDto
import dev.androhit.crosschat.chat.data.dto.MessageListDto
import dev.androhit.crosschat.chat.data.dto.UsersListDto
import dev.androhit.crosschat.data.network.CrossChatApi
import dev.androhit.crosschat.domain.model.ApiResponse
import io.ktor.client.call.body

class ChatRemoteDataSource(private val api: CrossChatApi) {

    suspend fun getAllChats(): ApiResponse<ChatListDto> {
        return api.get("chats")
    }

    suspend fun createChat(participantEmail: String): ApiResponse<ChatDto> {
        return api.post("chats", mapOf("email" to participantEmail)).body()
    }

    suspend fun getAllMessages(chatId: Int): ApiResponse<MessageListDto> {
        return api.get("chats/$chatId/messages")
    }

    suspend fun searchUsersByEmail(email: String): ApiResponse<UsersListDto> {
        return api.get("users/search?email=$email")
    }
}
