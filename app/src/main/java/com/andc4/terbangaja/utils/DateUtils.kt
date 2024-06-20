package com.andc4.terbangaja.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd - MMMM")
    private val fullDateFormatter = DateTimeFormatter.ofPattern("dd - MMMM - yyyy")

    fun formatIsoDate(isoDate: String): String {
        val instant = Instant.parse(isoDate)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return localDateTime.format(dateFormatter)
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
            else -> localDateTime.format(fullDateFormatter)
        }
    }
}
