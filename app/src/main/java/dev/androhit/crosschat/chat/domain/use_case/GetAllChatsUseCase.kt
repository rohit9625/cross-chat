package dev.androhit.crosschat.chat.domain.use_case

import dev.androhit.crosschat.chat.domain.ChatRepository
import dev.androhit.crosschat.chat.domain.model.Chat
import dev.androhit.crosschat.data.CredentialManager
import dev.androhit.crosschat.domain.model.DataError
import dev.androhit.crosschat.domain.model.Result

class GetAllChatsUseCase(
    private val repository: ChatRepository,
    private val credentialManager: CredentialManager,
) {
    suspend operator fun invoke(): Result<List<Chat>, DataError.Network> {
        val credentials = credentialManager.getAccessCredentials()
        val userId = credentials.userId ?: return Result.Error(DataError.Network.UNAUTHORIZED)

        return repository.getAllChats(userId)
    }
}