package dev.androhit.crosschat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.androhit.crosschat.auth.ui.AuthViewModel
import dev.androhit.crosschat.auth.ui.SignInScreen
import dev.androhit.crosschat.auth.ui.SignUpScreen

@Composable
fun AuthNavigation(
    onAuthenticated: () -> Unit
) {
    val authBackStack = rememberNavBackStack(Route.Auth.SignIn)
    val authViewModel = viewModel { AuthViewModel() }
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    NavDisplay(
        backStack = authBackStack,
        onBack = { authBackStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.Auth.SignIn> {
                SignInScreen(
                    uiState = uiState,
                    onEvent = authViewModel::onEvent,
                    onNavigateToSignUp = { authBackStack.add(Route.Auth.SignUp) },
                    onNavigateToHome = onAuthenticated
                )
            }
            entry<Route.Auth.SignUp> {
                SignUpScreen(
                    uiState = uiState,
                    onEvent = authViewModel::onEvent,
                    onNavigateToSignIn = { authBackStack.removeLastOrNull() },
                    onNavigateToHome = onAuthenticated
                )
            }
        }
    )
}