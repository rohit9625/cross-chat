package dev.androhit.crosschat.chat.data

import dev.androhit.crosschat.chat.data.dto.asEntity
import dev.androhit.crosschat.chat.data.local.ChatEntity
import dev.androhit.crosschat.chat.data.local.ChatLocalDataSource
import dev.androhit.crosschat.chat.data.remote.ChatRemoteDataSource
import dev.androhit.crosschat.chat.domain.ChatRepository
import dev.androhit.crosschat.chat.domain.model.Chat
import dev.androhit.crosschat.chat.domain.model.Message
import dev.androhit.crosschat.chat.domain.model.User
import dev.androhit.crosschat.data.CredentialManager
import dev.androhit.crosschat.domain.model.DataError
import dev.androhit.crosschat.domain.model.Result
import dev.androhit.crosschat.util.DateTimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class ChatRepositoryImpl(
    private val remoteDataSource: ChatRemoteDataSource,
    private val localDataSource: ChatLocalDataSource,
    private val socketClient: ChatSocketClient,
    private val credentialManager: CredentialManager,
): ChatRepository {
    override fun getAllChats(userId: Int): Flow<List<Chat>> {
        return localDataSource.getAllChats()
    }

    override suspend fun refreshChats(userId: Int): Result<Unit, DataError.Network> {
        return try {
            val response = remoteDataSource.getAllChats()
            if (response.success && response.data != null) {
                val chats = response.data.chats.map { chat ->
                    val displayName = chat.members.find { it.userId != userId }?.name ?: chat.type
                    val lastMessageTime = DateTimeUtils.parseUtcDateToLong(chat.lastMessage?.createdAt ?: chat.createdAt)

                    ChatEntity(
                        id = chat.id,
                        displayName = displayName,
                        lastMessageText = chat.lastMessage?.content,
                        lastMessageSender = chat.lastMessage?.senderName,
                        lastMessageTime = lastMessageTime,
                    )
                }
                localDataSource.upsertChats(chats)
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Network.SERVER_ERROR)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun createChat(participantEmail: String): Result<Chat, DataError.Network> {
        return try {
            val response = remoteDataSource.createChat(participantEmail)
            if (response.success && response.data != null) {
                val userId = credentialManager.getAccessCredentials().userId
                    ?: return Result.Error(DataError.Network.UNKNOWN)
                val chatDto = response.data
                val displayName = chatDto.members.find { it.userId != userId }?.name ?: chatDto.type
                val lastMessageTime = DateTimeUtils.parseUtcDateToLong(chatDto.lastMessage?.createdAt ?: chatDto.createdAt)

                localDataSource.upsertChats(listOf(ChatEntity(
                    id = chatDto.id,
                    displayName = displayName,
                    lastMessageText = chatDto.lastMessage?.content,
                    lastMessageSender = chatDto.lastMessage?.senderName,
                    lastMessageTime = lastMessageTime
                )))

                val domainChat = Chat(
                    id = chatDto.id,
                    displayName = displayName,
                    lastMessageText = null, // Simplified for immediate creation
                    lastMessageSender = chatDto.lastMessage?.senderName,
                    lastMessageTime = lastMessageTime,
                )
                Result.Success(domainChat)
            } else {
                Result.Error(DataError.Network.SERVER_ERROR)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override fun getMessagesForChat(chatId: Int): Flow<List<Message>> {
        return localDataSource.getMessagesForChat(chatId)
    }

    override suspend fun refreshMessages(chatId: Int): Result<Unit, DataError.Network> {
        return try {
            val response = remoteDataSource.getAllMessages(chatId)
            if (response.success && response.data != null) {
                val messages = response.data.messages.map { it.asEntity() }
                localDataSource.upsertMessages(messages)
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Network.SERVER_ERROR)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun searchUsersByEmail(email: String): Result<List<User>, DataError.Network> {
        return try {
            val response = remoteDataSource.searchUsersByEmail(email)
            if (response.success && response.data != null) {
                val users = response.data.users.map {
                    User(id = it.id, name = it.name, email = it.email)
                }
                Result.Success(users)
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

    override suspend fun observeMessages(chatId: Int) {
        val preferredLanguage = credentialManager.getAccessCredentials().preferredLanguage
        socketClient.observeMessages(chatId).collect { event ->
            when (event) {
                is SocketEvent.NewMessage -> {
                    localDataSource.upsertMessages(listOf(event.message.asEntity()))
                }
                is SocketEvent.MessageTranslated -> {
                    if(preferredLanguage == event.translation.language) {
                        localDataSource.updateMessageTranslation(
                            id = event.translation.messageId.toInt(),
                            translatedText = event.translation.translatedText,
                            status = "translated"
                        )
                    }
                }
            }
        }
    }
}
