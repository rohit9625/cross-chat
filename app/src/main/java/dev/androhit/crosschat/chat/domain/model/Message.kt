package dev.androhit.crosschat.chat.domain.model

data class Message(
    val id: Int,
    val text: String,
    val senderId: Int,
    val senderName: String,
    val autoTranslate: Boolean = false,
    val translationStatus: String? = null,
    val translatedText: String? = null,
    val translatedLanguage: String? = null,
    val timestamp: Long? = null,
)
