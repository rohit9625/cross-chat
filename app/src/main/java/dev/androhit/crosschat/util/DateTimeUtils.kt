package dev.androhit.crosschat.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateTimeUtils {
    private val dateIsoFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.US
    ).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun parseUtcDateToLong(utc: String): Long? {
        return try {
            dateIsoFormat.parse(utc)?.time
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun formatTime(timestamp: Long?): String? {
        return timestamp?.let {
            val formatter = SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            )
            return formatter.format(Date(it))
        }
    }

    fun formatSmartChatTime(timestamp: Long?): String {
        if (timestamp == null) return ""
        val date = Date(timestamp)

        val now = System.currentTimeMillis()
        val diff = now - timestamp

        val oneDay = 24 * 60 * 60 * 1000L

        return when {
            diff < oneDay -> {
                SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)
            }
            diff < 2 * oneDay -> "Yesterday"
            else -> {
                SimpleDateFormat("dd MMM", Locale.getDefault()).format(date)
            }
        }
    }
}
