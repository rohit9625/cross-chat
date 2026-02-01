package dev.androhit.crosschat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.androhit.crosschat.designsystem.ui.theme.CrossChatTheme
import dev.androhit.crosschat.navigation.NavigationRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Disable dynamic theme temporarily
            CrossChatTheme(dynamicColor = false) {
                NavigationRoot()
            }
        }
    }
}