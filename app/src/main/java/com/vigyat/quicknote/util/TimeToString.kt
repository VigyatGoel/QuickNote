package com.vigyat.quicknote.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertTimeStampToDateString(timeStamp: Long): String {
    val date = Date(timeStamp)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(date)
}