package dev.androhit.crosschat.chat.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.androhit.crosschat.R
import dev.androhit.crosschat.chat.domain.model.User
import dev.androhit.crosschat.chat.ui.event.CreateChatEvent
import dev.androhit.crosschat.chat.ui.states.CreateChatUiState
import dev.androhit.crosschat.designsystem.buttons.PrimaryButton
import dev.androhit.crosschat.designsystem.ui.theme.CrossChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateChatScreen(
    uiState: CreateChatUiState,
    onEvent: (CreateChatEvent) -> Unit,
    onProceedToChat: (Int, String) -> Unit = {_, _ -> },
    onNavigateBack: ()-> Unit = { },
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Create Chat") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { onEvent(CreateChatEvent.OnSearchQueryChange(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search for users by email") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = "Search"
                        )
                    },
                    shape = CircleShape,
                    singleLine = true
                )
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(uiState.searchedUsers) { user ->
                        ListItem(
                            headlineContent = { Text(user.name) },
                            supportingContent = { Text(user.email) },
                            modifier = Modifier
                                .clickable { onEvent(CreateChatEvent.OnUserSelected(user)) }
                                .padding(horizontal = 16.dp),
                            tonalElevation = if (uiState.selectedUser == user) 4.dp else 0.dp
                        )
                    }
                }
            }

            PrimaryButton(
                text = "Proceed to Chat",
                onClick = {
                    onEvent(CreateChatEvent.OnCreateChat(onProceedToChat))
                },
                enabled = uiState.selectedUser != null,
                shape = CircleShape,
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
                    .imePadding()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateChatScreenPreview() {
    CrossChatTheme(dynamicColor = false) {
        // Mock data for preview
        val users = remember {
            listOf(
                User(1, "Rohit Verma", "rohit@crosschat.dev"),
                User(2, "Jane Doe", "jane@crosschat.dev"),
                User(3, "John Smith", "john@crosschat.dev")
            )
        }
        CreateChatScreen(
            uiState = CreateChatUiState(
                searchQuery = "rohit",
                searchedUsers = users,
                selectedUser = users[0],
            ),
            onEvent = { /** Eat 5 Star, Do Nothing **/ },
        )
    }
}
