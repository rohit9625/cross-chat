package dev.androhit.crosschat.auth.domain

import dev.androhit.crosschat.domain.model.DataError
import dev.androhit.crosschat.domain.model.Result

typealias AuthResult = Result<Unit, DataError.Network>

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult
    suspend fun register(name: String, email: String, password: String): AuthResult
}