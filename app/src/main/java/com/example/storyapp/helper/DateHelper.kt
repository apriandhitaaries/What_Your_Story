package com.example.storyapp.helper

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DateHelper {
    fun formatDate(apiDateString: String): String {
        val apiFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val displayFormat = SimpleDateFormat("dd MMMM yyyy | HH:mm", Locale.getDefault())

        val utcTimeZone = TimeZone.getTimeZone("UTC")
        apiFormat.timeZone = utcTimeZone

        val wibTimeZone = TimeZone.getTimeZone("Asia/Jakarta")
        displayFormat.timeZone = wibTimeZone

        return try {
            val date = apiFormat.parse(apiDateString)
            displayFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
            "Tanggal tidak valid"
        }
    }
}