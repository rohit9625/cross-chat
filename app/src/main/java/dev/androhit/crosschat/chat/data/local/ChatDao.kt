package dev.androhit.crosschat.chat.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertChats(chats: List<ChatEntity>)

    @Query("SELECT * FROM chats")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMessages(messages: List<MessageEntity>)

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY createdAt DESC")
    fun getMessagesForChat(chatId: Int): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY createdAt DESC")
    suspend fun getMessagesForChatOnce(chatId: Int): List<MessageEntity>

    @Query("UPDATE messages SET translatedText = :translatedText, translationStatus = :status WHERE id = :id")
    suspend fun updateMessageTranslation(id: Int, translatedText: String, status: String)
}
