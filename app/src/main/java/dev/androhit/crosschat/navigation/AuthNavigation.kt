package dev.androhit.crosschat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.androhit.crosschat.auth.ui.viewmodel.SignUpViewModel
import dev.androhit.crosschat.auth.ui.SignInScreen
import dev.androhit.crosschat.auth.ui.SignUpScreen
import dev.androhit.crosschat.auth.ui.viewmodel.SignInViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthNavigation() {
    val authBackStack = rememberNavBackStack(Route.Auth.SignIn)

    NavDisplay(
        backStack = authBackStack,
        onBack = { authBackStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.Auth.SignIn> {
                val authViewModel = koinViewModel<SignInViewModel>()
                val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
                SignInScreen(
                    uiState = uiState,
                    onEvent = authViewModel::onEvent,
                    onNavigateToSignUp = { authBackStack.add(Route.Auth.SignUp) },
                    onNavigateToHome = {
                        // Remove auth navigation graph to avoid navigating back after
                        // successful authentication
                        authBackStack.remove(Route.Auth)
                        authBackStack.add(Route.Main)
                    }
                )
            }
            entry<Route.Auth.SignUp> {
                val authViewModel = koinViewModel<SignUpViewModel>()
                val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
                SignUpScreen(
                    uiState = uiState,
                    onEvent = authViewModel::onEvent,
                    onNavigateToSignIn = { authBackStack.removeLastOrNull() },
                    onNavigateToHome = {
                        authBackStack.remove(Route.Auth)
                        authBackStack.add(Route.Main)
                    }
                )
            }
        }
    )
}