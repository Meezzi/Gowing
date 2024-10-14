package com.meezzi.localtalk.util

import com.meezzi.localtalk.data.Time
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeFormat {

    fun timeFormat(): Time {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()).toString()
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()).toString()
        return Time(date, time)
    }
}