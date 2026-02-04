package dev.androhit.crosschat.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.androhit.crosschat.R
import dev.androhit.crosschat.auth.ui.event.SignInEvent
import dev.androhit.crosschat.auth.ui.state.SignInUiState
import dev.androhit.crosschat.designsystem.buttons.PrimaryButton
import dev.androhit.crosschat.designsystem.buttons.SimpleTextButton
import dev.androhit.crosschat.designsystem.textfields.PasswordTextField
import dev.androhit.crosschat.designsystem.textfields.SimpleTextField
import dev.androhit.crosschat.designsystem.texts.ErrorText
import dev.androhit.crosschat.designsystem.ui.theme.CrossChatTheme

@Composable
fun SignInScreen(
    uiState: SignInUiState,
    onEvent: (SignInEvent) -> Unit,
    onNavigateToSignUp: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    Scaffold { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.welcome_to))
                Text(
                    text = stringResource(R.string.app_name),
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(fontSize = 32.sp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(IntrinsicSize.Min)
            ) {
                SimpleTextField(
                    value = uiState.email,
                    onValueChange = { onEvent(SignInEvent.OnEmailChanged(it)) },
                    label = stringResource(R.string.email),
                    supportingText = uiState.emailError,
                )
                PasswordTextField(
                    value = uiState.password,
                    onValueChange = { onEvent(SignInEvent.OnPasswordChanged(it)) },
                    label = stringResource(R.string.password),
                    supportingText = uiState.passwordError,
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ErrorText(
                        error = uiState.generalError ?: ""
                    )
                    PrimaryButton(
                        text = stringResource(R.string.sign_in),
                        onClick = {
                            if(!uiState.isLoading) {
                                onEvent(SignInEvent.OnSubmit(onSuccess = onNavigateToHome))
                            }
                        },
                        isLoading = uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = stringResource(R.string.new_to_cross_chat))
                SimpleTextButton(
                    text = stringResource(R.string.sign_up),
                    onClick = {
                        if(!uiState.isLoading) {
                            onNavigateToSignUp()
                        }
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenPreview() {
    CrossChatTheme(dynamicColor = false) {
        SignInScreen(
            uiState = SignInUiState(
                email = "test@example.com",
                password = "test_password"
            ),
            onEvent = { /** Eat 5 Star, Do Nothing **/ }
        )
    }
}