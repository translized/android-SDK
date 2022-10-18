package com.translized.translized_ota.local

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TranslizedUtils {
    companion object {
        fun getISO8601StringForCurrentDate(): String {
            val now = Date()
            return getISO8601StringForDate(now) ?: ""
        }

        private fun getISO8601StringForDate(date: Date): String? {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            return dateFormat.format(date)
        }
    }
}