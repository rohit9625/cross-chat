package dev.androhit.crosschat.data

import android.content.Context
import androidx.datastore.dataStore
import dev.androhit.crosschat.domain.model.AccessCredentials
import dev.androhit.crosschat.util.CredentialSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

private val Context.dataStore by dataStore(
    fileName = "credentials",
    serializer = CredentialSerializer
)

class CredentialManager(private val context: Context) {
    private var accessToken: String? = null
    private var refreshToken: String? = null

    val credentialsFlow: Flow<AccessCredentials> = context.dataStore.data

    suspend fun saveAccessToken(token: String) {
        accessToken = token
        context.dataStore.updateData {
            it.copy(accessToken = token)
        }
    }

    suspend fun getAccessToken(): String? {
        return accessToken ?: context.dataStore.data.first().accessToken
    }

    suspend fun saveAccessCredentials(credentials: AccessCredentials) {
        context.dataStore.updateData { credentials }
    }

    suspend fun getAccessCredentials(): AccessCredentials {
        return context.dataStore.data.first()
    }

    suspend fun updatePreferredLanguage(language: String) {
        context.dataStore.updateData {
            it.copy(preferredLanguage = language)
        }
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
