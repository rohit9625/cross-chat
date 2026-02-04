package dev.androhit.crosschat.auth.data

import android.util.Log
import dev.androhit.crosschat.auth.data.model.AuthResponse
import dev.androhit.crosschat.auth.data.model.LoginRequest
import dev.androhit.crosschat.auth.data.model.RegisterRequest
import dev.androhit.crosschat.auth.domain.AuthRepository
import dev.androhit.crosschat.data.CredentialManager
import dev.androhit.crosschat.data.network.CrossChatApi
import dev.androhit.crosschat.domain.model.DataError
import dev.androhit.crosschat.domain.model.Result
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

typealias AuthResult = Result<Unit, DataError.Network>

class AuthRepositoryImpl(
    private val api: CrossChatApi,
    private val credentialManager: CredentialManager,
) : AuthRepository {
    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            val response = api.post(
                path = "auth/login",
                body = LoginRequest(email, password)
            )

            when (response.status.value) {
                in 200..299 -> {
                    val authResponse = response.body<AuthResponse>()
                    credentialManager.saveAccessToken(authResponse.token)
                    Result.Success(Unit)
                }
                400 -> Result.Error(DataError.Network.BAD_REQUEST)
                429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
                else -> Result.Error(DataError.Network.UNKNOWN)
            }
        } catch (e: Exception) {
            Result.Error(getErrorFromException(e))
        }
    }

    override suspend fun register(name: String, email: String, password: String): AuthResult {
        return try {
            val response = api.post(
                path = "auth/register",
                body = RegisterRequest(name, email, password)
            )

            when (response.status.value) {
                in 200..299 -> {
                    val authResponse = response.body<AuthResponse>()
                    credentialManager.saveAccessToken(authResponse.token)
                    Result.Success(Unit)
                }
                400 -> Result.Error(DataError.Network.BAD_REQUEST)
                401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
                else -> Result.Error(DataError.Network.UNKNOWN)
            }
        } catch (e: Exception) {
            Result.Error(getErrorFromException(e))
        }
    }

    private fun getErrorFromException(e: Exception): DataError.Network {
        return when(e) {
            is ConnectTimeoutException -> {
                Log.e(TAG, "Server unreachable: ${e.message}")
                DataError.Network.REQUEST_TIMEOUT
            }
            is UnresolvedAddressException -> DataError.Network.NO_INTERNET
            is SerializationException -> {
                Log.e(TAG, "Failed to parse server response", e)
                DataError.Network.SERIALIZATION
            }
            else -> {
                Log.e(TAG, "Unexpected error", e)
                DataError.Network.UNKNOWN
            }
        }
    }

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}