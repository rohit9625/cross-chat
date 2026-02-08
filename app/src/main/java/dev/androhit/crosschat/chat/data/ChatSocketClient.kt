package dev.androhit.crosschat.chat.data

import android.util.Log
import dev.androhit.crosschat.BuildConfig
import dev.androhit.crosschat.chat.data.dto.MessageDto
import dev.androhit.crosschat.chat.data.dto.MessageTranslatedDto
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import org.json.JSONObject

sealed class SocketEvent {
    data class NewMessage(val message: MessageDto) : SocketEvent()
    data class MessageTranslated(val translation: MessageTranslatedDto) : SocketEvent()
}

class ChatSocketClient(private val json: Json) {
    private var socket: Socket? = null

    fun connect(token: String) {
        val options = IO.Options.builder()
            .setAuth(mapOf("token" to token))
            .setReconnection(true)
            .build()

        socket = IO.socket(BuildConfig.BASE_URL, options)
        socket?.connect()
        Log.i(TAG, "Connected to socket")
    }

    fun disconnect() {
        Log.i(TAG, "Closing socket connection")
        socket?.disconnect()
        socket?.off()
        socket = null
    }

    fun sendMessage(chatId: Int, text: String) {
        val messageJson = JSONObject().apply {
            put("chat_id", chatId)
            put("text", text)
        }
        socket?.emit(EVENT_SEND_MESSAGE, messageJson)
    }

    fun observeMessages(chatId: Int): Flow<SocketEvent> = callbackFlow {
        val messageListener = Emitter.Listener { args ->
            val data = args[0] as JSONObject
            val messageDto = json.decodeFromString<MessageDto>(data.toString())
            trySend(SocketEvent.NewMessage(messageDto))
        }

        val translationListener = Emitter.Listener { args ->
            val data = args[0] as JSONObject
            val translationDto = json.decodeFromString<MessageTranslatedDto>(data.toString())
            trySend(SocketEvent.MessageTranslated(translationDto))
        }

        if (socket?.connected() == true) {
            socket?.emit(EVENT_JOIN_CHAT, chatId)
        } else {
            socket?.once(Socket.EVENT_CONNECT) {
                socket?.emit(EVENT_JOIN_CHAT, chatId)
            }
        }

        socket?.on(EVENT_NEW_MESSAGE, messageListener)
        socket?.on(EVENT_MESSAGE_TRANSLATED, translationListener)

        awaitClose {
            socket?.off(EVENT_NEW_MESSAGE, messageListener)
            socket?.off(EVENT_MESSAGE_TRANSLATED, translationListener)
        }
    }

    companion object {
        private const val TAG = "ChatSocketClient"
        private const val EVENT_SEND_MESSAGE = "send_message"
        private const val EVENT_NEW_MESSAGE = "new_message"
        private const val EVENT_JOIN_CHAT = "join_chat"
        private const val EVENT_MESSAGE_TRANSLATED = "message_translated"
    }
}
