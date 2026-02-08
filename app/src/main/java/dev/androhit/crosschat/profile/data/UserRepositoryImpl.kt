package dev.androhit.crosschat.profile.data

import dev.androhit.crosschat.data.CredentialManager
import dev.androhit.crosschat.data.network.CrossChatApi
import dev.androhit.crosschat.domain.model.DataError
import dev.androhit.crosschat.domain.model.Result
import dev.androhit.crosschat.profile.data.model.LanguageUpdateRequest
import dev.androhit.crosschat.profile.domain.UserRepository

class UserRepositoryImpl(
    private val api: CrossChatApi,
    private val credentialManager: CredentialManager
) : UserRepository {
    override suspend fun updatePreferredLanguage(language: String): Result<Unit, DataError.Network> {
        return try {
            val response = api.patch(
                path = "users/me/language",
                body = LanguageUpdateRequest(preferredLanguage = language)
            )

            if (response.status.value in 200..299) {
                credentialManager.updatePreferredLanguage(language)
                Result.Success(Unit)
            } else {
                when (response.status.value) {
                    401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                    in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
                    else -> Result.Error(DataError.Network.UNKNOWN)
                }
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}
