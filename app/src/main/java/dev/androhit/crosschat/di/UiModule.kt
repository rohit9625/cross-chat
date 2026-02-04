package dev.androhit.crosschat.di

import dev.androhit.crosschat.auth.ui.viewmodel.SignInViewModel
import dev.androhit.crosschat.auth.ui.viewmodel.SignUpViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SignInViewModel)
}