package dev.androhit.crosschat.di

import dev.androhit.crosschat.BuildConfig
import dev.androhit.crosschat.auth.data.AuthRepositoryImpl
import dev.androhit.crosschat.auth.domain.AuthRepository
import dev.androhit.crosschat.chat.data.ChatRepositoryImpl
import dev.androhit.crosschat.chat.domain.ChatRepository
import dev.androhit.crosschat.data.CredentialManager
import dev.androhit.crosschat.data.network.CrossChatApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val BASE_URL = "${BuildConfig.BASE_URL}/api/"

val networkModule = module {
    singleOf(::CrossChatApi)
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::ChatRepositoryImpl) bind ChatRepository::class

    single<HttpClient> {
        val credentialManager = get<CredentialManager>()
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(json = Json { ignoreUnknownKeys = true })
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 5000L
                requestTimeoutMillis = 5000L
                connectTimeoutMillis = 5000L
            }

            install(DefaultRequest) {
                url(BASE_URL)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val token = credentialManager.getAccessCredentials().accessToken
                        if (token != null) {
                            BearerTokens(accessToken = token, refreshToken =  null)
                        } else {
                            null
                        }
                    }
                }
            }

            install(Logging) {
                logger = Logger.ANDROID
                level = if(BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
            }
        }
    }
}
