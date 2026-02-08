package dev.androhit.crosschat.di

import androidx.room.Room
import dev.androhit.crosschat.chat.data.ChatSocketClient
import dev.androhit.crosschat.chat.data.local.ChatDatabase
import dev.androhit.crosschat.chat.data.local.ChatLocalDataSource
import dev.androhit.crosschat.chat.data.remote.ChatRemoteDataSource
import dev.androhit.crosschat.data.CredentialManager
import dev.androhit.crosschat.util.Constants
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::CredentialManager)
    singleOf(::ChatSocketClient)
    single { Json { ignoreUnknownKeys = true } }
    single {
        Room.databaseBuilder(
                get(),
                ChatDatabase::class.java,
                Constants.DB_NAME
            ).build()
    }
    single { get<ChatDatabase>().dao }
    singleOf(::ChatLocalDataSource)
    singleOf(::ChatRemoteDataSource)
}
