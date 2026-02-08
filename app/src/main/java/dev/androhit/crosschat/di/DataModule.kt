package dev.androhit.crosschat.di

import androidx.room.Room
import dev.androhit.crosschat.chat.data.ChatSocketClient
import dev.androhit.crosschat.chat.data.local.ChatDatabase
import dev.androhit.crosschat.chat.domain.use_case.GetAllChatsUseCase
import dev.androhit.crosschat.data.CredentialManager
import dev.androhit.crosschat.util.Constants
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::CredentialManager)
    factoryOf(::GetAllChatsUseCase)
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
}
