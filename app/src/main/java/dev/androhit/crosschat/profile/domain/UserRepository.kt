package dev.androhit.crosschat.profile.domain

import dev.androhit.crosschat.domain.model.DataError
import dev.androhit.crosschat.domain.model.Result

interface UserRepository {
    suspend fun updatePreferredLanguage(language: String): Result<Unit, DataError.Network>
}
