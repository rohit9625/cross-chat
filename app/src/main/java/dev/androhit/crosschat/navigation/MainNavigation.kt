package dev.androhit.crosschat.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.androhit.crosschat.R
import dev.androhit.crosschat.chat.ui.screens.ChatListScreen
import dev.androhit.crosschat.chat.ui.screens.CreateChatScreen
import dev.androhit.crosschat.chat.ui.screens.MessagingScreen
import dev.androhit.crosschat.chat.ui.viewmodels.ChatListViewModel
import dev.androhit.crosschat.chat.ui.viewmodels.CreateChatViewModel
import dev.androhit.crosschat.chat.ui.viewmodels.MessagingViewModel
import dev.androhit.crosschat.profile.ui.screens.MyProfileScreen
import dev.androhit.crosschat.profile.ui.viewmodels.MyProfileViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MainNavigation() {
    val mainBackStack = rememberNavBackStack(Route.Main.Home)
    Scaffold(
        bottomBar = {
            val currentRoute = mainBackStack.lastOrNull()
            if (currentRoute is Route.Main.Home || currentRoute is Route.Main.Profile) {
                MainBottomNavigation(mainBackStack)
            }
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
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
                            mainBackStack.remove(Route.Main.NewChat)
                        },
                        onNavigateBack = { mainBackStack.removeLastOrNull() }
                    )
                }
                entry<Route.Main.Profile> {
                    val viewModel = koinViewModel<MyProfileViewModel>()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    MyProfileScreen(
                        uiState = uiState,
                        onLanguageSelected = viewModel::updateLanguage,
                        onNavigateBack = { mainBackStack.removeLastOrNull() }
                    )
                }
            }
        )
    }
}

@Composable
fun MainBottomNavigation(backStack: NavBackStack<NavKey>) {
    val currentRoute = backStack.lastOrNull()
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute is Route.Main.Home,
            onClick = {
                if (currentRoute !is Route.Main.Home) {
                    backStack.add(Route.Main.Home)
                }
            },
            icon = { Icon(painter = painterResource(R.drawable.ic_home_outlined), contentDescription = "Home") },
            label = { Text("Chats") }
        )
        NavigationBarItem(
            selected = currentRoute is Route.Main.Profile,
            onClick = {
                if (currentRoute !is Route.Main.Profile) {
                    backStack.add(Route.Main.Profile)
                }
            },
            icon = { Icon(painter = painterResource(R.drawable.ic_outline_person), contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}
