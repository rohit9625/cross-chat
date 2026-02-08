package dev.androhit.crosschat.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @Serializable
    data object Auth: Route, NavKey {
        @Serializable
        data object SignIn: Route, NavKey

        @Serializable
        data object SignUp: Route, NavKey
    }

    @Serializable
    data object Main: Route, NavKey {
        @Serializable
        data object Home: Route, NavKey
        @Serializable
        data object Profile: Route, NavKey
        @Serializable
        data class Chat(val chatId: Int, val chatTitle: String): Route, NavKey
        @Serializable
        data object NewChat: Route, NavKey
    }
}
