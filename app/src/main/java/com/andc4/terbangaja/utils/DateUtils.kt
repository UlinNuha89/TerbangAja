package com.andc4.terbangaja.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM-yyyy")

    fun formatDate(isoDate: String?): String {
        val instant = Instant.parse(isoDate)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return localDateTime.format(dateFormatter)
    }

    fun formatTime(isoDate: String?): String {
        val instant = Instant.parse(isoDate)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return localDateTime.format(timeFormatter)
    }

    fun formatIsoDate(isoDate: String): String {
        val instant = Instant.parse(isoDate)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return localDateTime.format(monthFormatter)
    }

    fun timeAgo(isoDate: String): String {
        val instant = Instant.parse(isoDate)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val now = LocalDateTime.now()
        val duration = ChronoUnit.MINUTES.between(localDateTime, now)

        return when {
            duration < 60 -> "$duration menit yang lalu"
            duration < 1440 -> "${duration / 60} jam yang lalu"
            duration < 2880 -> "Kemarin"
            duration < 4320 -> "${duration / 1440} hari yang lalu"
            else -> localDateTime.format(dateFormatter)
        }
    }

    private fun String.toDate(): Date? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.parse(this)
    }

    private fun Date.toMonthString(): String {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        return dateFormat.format(this)
    }
}
