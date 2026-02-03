package dev.androhit.crosschat.util

import androidx.datastore.core.Serializer
import dev.androhit.crosschat.domain.model.AccessCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

object CredentialSerializer: Serializer<AccessCredentials> {
    override val defaultValue: AccessCredentials
        get() = AccessCredentials()

    override suspend fun readFrom(input: InputStream): AccessCredentials {
        val encrypted = withContext(Dispatchers.IO) {
            input.use { it.readBytes() }
        }
        val encryptedBytesDecoded = Base64.getDecoder().decode(encrypted)
        val decryptedBytes = Encryptor.decrypt(encryptedBytesDecoded)
        val json = decryptedBytes.decodeToString()
        return Json.decodeFromString(json)
    }

    override suspend fun writeTo(t: AccessCredentials, output: OutputStream) {
        val json = Json.encodeToString(t)
        val bytes = json.toByteArray()
        val encryptedBytes = Encryptor.encrypt(bytes)
        val encryptedBase64 = Base64.getEncoder().encode(encryptedBytes)
        withContext(Dispatchers.IO) {
            output.use {
                it.write(encryptedBase64)
            }
        }
    }
}