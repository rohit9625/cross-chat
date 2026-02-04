package dev.androhit.crosschat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.androhit.crosschat.data.CredentialManager
import dev.androhit.crosschat.designsystem.ui.theme.CrossChatTheme
import dev.androhit.crosschat.navigation.NavigationRoot
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val credentialManager by inject<CredentialManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Using blocking operation to check if user is authenticated
        // TODO("Must remove this blocking call after splash screen is setup)
        val isAuthenticated = runBlocking {
            credentialManager.getAccessToken() != null
        }
        setContent {
            // Disable dynamic theme temporarily
            CrossChatTheme(dynamicColor = false) {
                NavigationRoot(isAuthenticated)
            }
        }
    }
}