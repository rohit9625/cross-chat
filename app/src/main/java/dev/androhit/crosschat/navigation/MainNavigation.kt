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
import dev.androhit.crosschat.chat.ui.screens.CreateChatScreen
import dev.androhit.crosschat.chat.ui.screens.MessagingScreen
import dev.androhit.crosschat.chat.ui.viewmodels.ChatListViewModel
import dev.androhit.crosschat.chat.ui.viewmodels.CreateChatViewModel
import dev.androhit.crosschat.chat.ui.viewmodels.MessagingViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

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
                ChatListScreen(
                    uiState = uiState,
                    onOpenChat = { chatId, chatTitle ->
                        mainBackStack.add(Route.Main.Chat(chatId, chatTitle))
                    },
                    onNewChat = { mainBackStack.add(Route.Main.NewChat) }
                )
            }
            entry<Route.Main.Chat> {
                val viewModel = koinViewModel<MessagingViewModel>(
                    parameters = { parametersOf(it.chatId, it.chatTitle) }
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                MessagingScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    onNavigateBack = {
                        mainBackStack.removeLastOrNull()
                    }
                )
            }
            entry<Route.Main.NewChat> {
                val viewModel = koinViewModel<CreateChatViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                CreateChatScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    onProceedToChat = { chatId, chatTitle ->
                        mainBackStack.add(Route.Main.Chat(chatId, chatTitle))
                    },
                    onNavigateBack = { mainBackStack.removeLastOrNull() }
                )
            }
        }
    )
}