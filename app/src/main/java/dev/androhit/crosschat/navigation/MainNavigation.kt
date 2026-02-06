package dev.androhit.crosschat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.androhit.crosschat.chat.ui.screens.ChatListScreen
import dev.androhit.crosschat.chat.ui.viewmodels.ChatListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainNavigation() {
    val mainBackStack = rememberNavBackStack(Route.Main.Home)

    NavDisplay(
        backStack = mainBackStack,
        onBack = { mainBackStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Route.Main.Home> {
                val viewModel = koinViewModel<ChatListViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                ChatListScreen(uiState)
            }
        }
    )
}