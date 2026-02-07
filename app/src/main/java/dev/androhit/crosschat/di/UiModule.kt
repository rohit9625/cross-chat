package dev.androhit.crosschat.di

import dev.androhit.crosschat.auth.ui.viewmodel.SignInViewModel
import dev.androhit.crosschat.auth.ui.viewmodel.SignUpViewModel
import dev.androhit.crosschat.chat.ui.viewmodels.ChatListViewModel
import dev.androhit.crosschat.chat.ui.viewmodels.CreateChatViewModel
import dev.androhit.crosschat.chat.ui.viewmodels.MessagingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::ChatListViewModel)
    viewModel { parameters ->
        MessagingViewModel(
            chatId = parameters.get(),
            chatTitle = parameters.get(),
            repository = get(),
            credentialManager = get()
        )
    }
    viewModelOf(::CreateChatViewModel)
}
