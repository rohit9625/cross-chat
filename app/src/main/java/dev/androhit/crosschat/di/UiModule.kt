package dev.androhit.crosschat.di

import dev.androhit.crosschat.auth.ui.viewmodel.SignInViewModel
import dev.androhit.crosschat.auth.ui.viewmodel.SignUpViewModel
import dev.androhit.crosschat.chat.ui.viewmodels.ChatListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::ChatListViewModel)
}