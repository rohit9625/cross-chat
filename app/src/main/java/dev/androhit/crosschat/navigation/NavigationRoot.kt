package dev.androhit.crosschat.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

@Composable
fun NavigationRoot(isAuthenticated: Boolean = false) {
    val rootBackStack = rememberNavBackStack(
        if(isAuthenticated) Route.Main else Route.Auth
    )

    NavDisplay(
        backStack = rootBackStack,
        onBack = { rootBackStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.Main> {
                MainNavigation()
            }

            entry<Route.Auth> {
                AuthNavigation(
                    onAuthenticated = {
                        rootBackStack.remove(Route.Auth)
                        rootBackStack.add(Route.Main)
                    }
                )
            }
        }
    )
}