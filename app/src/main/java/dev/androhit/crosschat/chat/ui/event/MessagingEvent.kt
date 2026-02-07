package dev.androhit.crosschat.chat.ui.event

sealed interface MessagingEvent {
    data class OnMessageChanged(val message: String) : MessagingEvent
    data object OnSendMessage : MessagingEvent
}
