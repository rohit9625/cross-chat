package dev.androhit.crosschat.data

import android.content.Context
import androidx.datastore.dataStore
import dev.androhit.crosschat.util.CredentialSerializer
import kotlinx.coroutines.flow.first

private val Context.dataStore by dataStore(
    fileName = "credentials",
    serializer = CredentialSerializer
)

class CredentialManager(private val context: Context) {
    private var accessToken: String? = null
    private var refreshToken: String? = null

    suspend fun saveAccessToken(token: String) {
        accessToken = token
        context.dataStore.updateData {
            it.copy(accessToken = token)
        }
    }

    suspend fun getAccessToken(): String? {
        return accessToken ?: context.dataStore.data.first().accessToken
    }

    suspend fun saveRefreshToken(token: String) {
        refreshToken = token
        context.dataStore.updateData {
            it.copy(refreshToken = token)
        }
    }

    suspend fun getRefreshToken(): String? {
        return refreshToken ?: context.dataStore.data.first().refreshToken
    }
}