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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.androhit.crosschat.designsystem.buttons.PrimaryButton
import dev.androhit.crosschat.designsystem.buttons.SimpleTextButton
import dev.androhit.crosschat.designsystem.textfields.SimpleTextField
import dev.androhit.crosschat.designsystem.textfields.PasswordTextField
import dev.androhit.crosschat.designsystem.ui.theme.CrossChatTheme

@Composable
fun SignInScreen(
    uiState: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
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
                Text(text = "Welcome to")
                Text(text = "CrossChat", color = MaterialTheme.colorScheme.primary, style = TextStyle(
                    fontSize = 32.sp,
                ))
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .width(IntrinsicSize.Min)
            ) {
                SimpleTextField(
                    value = uiState.email,
                    onValueChange = { onEvent(AuthEvent.OnEmailChanged(it)) },
                    label = "Email",
                )
                PasswordTextField(
                    value = uiState.password,
                    onValueChange = { onEvent(AuthEvent.OnPasswordChanged(it)) },
                    label = "Password",
                )

                PrimaryButton(
                    text = "Sign In",
                    onClick = {
                        onEvent(
                            AuthEvent.OnSubmit(
                                action = SubmitAction.SIGN_IN,
                                onSuccess = onNavigateToHome
                            )
                        )
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "Doesn't have an account?")
                SimpleTextButton(
                    text = "Sign Up",
                    onClick = onNavigateToSignUp,
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
            uiState = AuthUiState(
                email = "test@example.com",
                password = "test_password"
            ),
            onEvent = { /** Eat 5 Star, Do Nothing **/ }
        )
    }
}