package dev.androhit.crosschat.chat.data.local

import dev.androhit.crosschat.chat.domain.model.Chat
import dev.androhit.crosschat.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatLocalDataSource(private val dao: ChatDao) {

    fun getAllChats(): Flow<List<Chat>> {
        return dao.getAllChats().map { entities ->
            entities.map { entity ->
                Chat(
                    id = entity.id,
                    displayName = entity.displayName,
                    lastMessageText = entity.lastMessageText,
                    lastMessageSender = entity.displayName,
                    lastMessageTime = entity.lastMessageTime
                )
            }
        }
    }

    suspend fun upsertChats(chats: List<ChatEntity>) {
        dao.upsertChats(chats)
    }

    fun getMessagesForChat(chatId: Int): Flow<List<Message>> {
        return dao.getMessagesForChat(chatId).map { entities ->
            entities.map { it.toMessage() }
        }
    }

    suspend fun upsertMessages(messages: List<MessageEntity>) {
        dao.upsertMessages(messages)
    }

    suspend fun updateMessageTranslation(id: Int, translatedText: String, status: String) {
        dao.updateMessageTranslation(id, translatedText, status)
    }
}
