package com.andc4.terbangaja.utils

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

object DateUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale("id", "ID"))
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale("id", "ID"))
    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM-yyyy", Locale("id", "ID"))
    private val dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

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

    fun calculateDuration(
        departureTime: String,
        arrivalTime: String,
    ): String {
        val departure = ZonedDateTime.parse(departureTime, dateTimeFormatter)
        val arrival = ZonedDateTime.parse(arrivalTime, dateTimeFormatter)
        val duration = Duration.between(departure, arrival)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60

        return String.format(Locale.US, "%2dJ:%2dM", hours, minutes)
    }

    private fun String.toLocalDateIndonesia(): LocalDate? {
        val dateFormat =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val zonedDateTime =
            ZonedDateTime.parse(this, dateFormat.withZone(ZoneId.of("UTC")))
        val localDate = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Jakarta")).toLocalDate()
        return localDate
    }

    private fun Date.toMonthString(): String {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        return dateFormat.format(this)
    }
}
