package dev.androhit.crosschat.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateTimeUtils {
    private val apiDateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.US
    ).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun parseUtcDate(utc: String): Date? {
        return try {
            apiDateFormat.parse(utc)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun formatTime(date: Date?): String? {
        return date?.let {
            val formatter = SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            )
            return formatter.format(it)
        } ?: ""
    }

    fun formatSmartChatTime(utc: String): String {
        val date = parseUtcDate(utc) ?: return ""

        val now = System.currentTimeMillis()
        val diff = now - date.time

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


