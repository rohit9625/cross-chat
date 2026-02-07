package dev.androhit.crosschat.chat.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.androhit.crosschat.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.androhit.crosschat.chat.ui.states.ChatListUiState
import dev.androhit.crosschat.chat.ui.states.ChatUiState
import dev.androhit.crosschat.designsystem.ui.theme.CrossChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    uiState: ChatListUiState,
    onOpenChat: (Int, String) -> Unit = { _, _ -> },
    onNewChat: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Chats") },
                actions = {
                    IconButton(onClick = { /* Handle logout */ }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_logout),
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNewChat,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Add"
                )
                Text(text = "New Chat", style = MaterialTheme.typography.titleMedium)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(uiState.chats, key = { it.id}) { chat ->
                ChatListItem(
                    chat = chat,
                    modifier = Modifier.clickable {
                        onOpenChat(chat.id, chat.title)
                    }
                )
            }
        }
    }
}

@Composable
fun ChatListItem(
    chat: ChatUiState,
    modifier: Modifier = Modifier
) {
    val text = remember(chat.title) {
        chat.title
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2).joinToString("") { it.first().uppercase() }
            .ifEmpty { "?" } // Fallback if name is empty
    }
    ListItem(
        headlineContent = { Text(chat.title) },
        supportingContent = { Text(chat.subTitle) },
        leadingContent = {
            Card(
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        },
        trailingContent = { chat.lastMessageTime?.let { Text(text = it) } },
        modifier = modifier
    )
}

@Preview
@Composable
private fun ChatListScreenPreview() {
    CrossChatTheme(dynamicColor = false) {
        ChatListScreen(
            uiState = ChatListUiState(
                chats = listOf(
                    ChatUiState(1, "Rohit Verma", "Hello", "18:36"),
                    ChatUiState(2, "Tom Holland", "Hey buddy", "19:00"),
                    ChatUiState(3, "Captain America", "I can do this all day", "19:07"),
                )
            )
        )
    }
}
