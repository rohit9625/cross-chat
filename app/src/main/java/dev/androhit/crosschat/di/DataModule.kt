package dev.androhit.crosschat.di

import dev.androhit.crosschat.data.CredentialManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::CredentialManager)
}