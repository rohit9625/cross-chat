package dev.androhit.crosschat.chat.data

import dev.androhit.crosschat.chat.data.dto.ChatListDto
import dev.androhit.crosschat.chat.domain.ChatRepository
import dev.androhit.crosschat.chat.domain.model.Chat
import dev.androhit.crosschat.chat.domain.model.Message
import dev.androhit.crosschat.data.network.CrossChatApi
import dev.androhit.crosschat.domain.model.ApiResponse
import dev.androhit.crosschat.domain.model.DataError
import dev.androhit.crosschat.domain.model.Result
import dev.androhit.crosschat.util.DateTimeUtils

class ChatRepositoryImpl(
    private val api: CrossChatApi,
): ChatRepository {
    override suspend fun getAllChats(userId: Int): Result<List<Chat>, DataError.Network> {
        return try {
            val response = api.get<ApiResponse<ChatListDto>>("chats")

            if (response.success && response.data != null) {
                val chats = response.data.chats.map { chat ->
                    val displayName = chat.members.find { it.userId != userId }?.name ?: chat.type
                    val formattedTime = DateTimeUtils.parseUtcDate(chat.lastMessage?.createdAt ?: chat.createdAt)

                    val lastMessage = chat.lastMessage?.let { message ->
                        val sender = chat.members.find { it.userId == message.senderId }
                        val senderName = when {
                            sender == null -> "Unknown"
                            sender.userId == userId -> "You"
                            else -> sender.name
                        }
                        Message(
                            text = message.content,
                            senderId = message.senderId,
                            senderName = senderName
                        )
                    }

                    Chat(
                        id = chat.id,
                        displayName = displayName,
                        lastMessage = lastMessage,
                        lastMessageTime = formattedTime,
                    )
                }
                Result.Success(data = chats)
            } else {
                Result.Error(DataError.Network.SERVER_ERROR)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun createChat(participantEmail: String): Result<Chat, DataError.Network> {
        TODO("Not yet implemented")
    }
}
