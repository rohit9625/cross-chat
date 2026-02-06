package dev.androhit.crosschat.chat.data

import dev.androhit.crosschat.chat.data.dto.ChatListDto
import dev.androhit.crosschat.chat.data.dto.MessageListDto
import dev.androhit.crosschat.chat.domain.ChatRepository
import dev.androhit.crosschat.chat.domain.model.Chat
import dev.androhit.crosschat.chat.domain.model.Message
import dev.androhit.crosschat.data.CredentialManager
import dev.androhit.crosschat.data.network.CrossChatApi
import dev.androhit.crosschat.domain.model.ApiResponse
import dev.androhit.crosschat.domain.model.DataError
import dev.androhit.crosschat.domain.model.Result
import dev.androhit.crosschat.util.DateTimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(
    private val api: CrossChatApi,
    private val socketClient: ChatSocketClient,
    private val credentialManager: CredentialManager,
): ChatRepository {
    override suspend fun getAllChats(userId: Int): Result<List<Chat>, DataError.Network> {
        return try {
            val response = api.get<ApiResponse<ChatListDto>>("chats")

            if (response.success && response.data != null) {
                val chats = response.data.chats.map { chat ->
                    val displayName = chat.members.find { it.userId != userId }?.name ?: chat.type
                    val lastMessageTime = DateTimeUtils.parseUtcDate(chat.lastMessage?.createdAt ?: chat.createdAt)

                    val lastMessage = chat.lastMessage?.let { message ->
                        val sender = chat.members.find { it.userId == message.senderId }
                        val senderName = when {
                            sender == null -> "Unknown"
                            sender.userId == userId -> "You"
                            else -> sender.name
                        }
                        Message(
                            id = message.id,
                            text = message.content,
                            senderId = message.senderId,
                            senderName = senderName,
                            timestamp = lastMessageTime,
                        )
                    }

                    Chat(
                        id = chat.id,
                        displayName = displayName,
                        lastMessage = lastMessage,
                        lastMessageTime = lastMessageTime,
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

    override suspend fun getAllMessages(chatId: Int): Result<List<Message>, DataError.Network> {
        return try {
            val response = api.get<ApiResponse<MessageListDto>>("chats/$chatId/messages")

            if (response.success && response.data != null) {
                val chats = response.data.messages.map { msg ->
                    Message(
                        id = msg.id,
                        text = msg.content,
                        senderId = msg.senderId,
                        timestamp = DateTimeUtils.parseUtcDate(msg.createdAt),
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

    override suspend fun connectToSocket() {
        credentialManager.getAccessToken()?.let {
            socketClient.connect(it)
        }
    }

    override fun disconnectFromSocket() {
        socketClient.disconnect()
    }

    override fun sendMessage(chatId: Int, text: String) {
        socketClient.sendMessage(chatId, text)
    }

    override fun observeMessages(chatId: Int): Flow<Message> {
        return socketClient.observeMessages(chatId).map {
            Message(
                id = it.id,
                text = it.content,
                senderId = it.senderId,
                timestamp = DateTimeUtils.parseUtcDate(it.createdAt),
            )
        }
    }
}
