package com.meezzi.localtalk.util

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
}
