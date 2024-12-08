package com.meezzi.localtalk.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeFormat {

    fun getDate(): String {
        return SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date()).toString()
    }

    fun getTime(): String {
        return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()).toString()
    }

    fun toFormattedString(timeString: Timestamp): String {
        val date = timeString.toDate()
        val format = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return format.format(date)
    }

    fun formatMessageTime(timestamp: Timestamp): String {
        return try {
            val timeFormat = SimpleDateFormat("a h:mm", Locale.getDefault())
            timestamp.toDate().let { timeFormat.format(it) }
        } catch (e: Exception) {
            "시간 없음"
        }
    }
}
