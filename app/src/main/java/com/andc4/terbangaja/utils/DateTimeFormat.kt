package com.andc4.terbangaja.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime?.toIndonesianTime(): String? {
    return try {
        val date = this?.toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))
        return date?.format(formatter)
    } catch (e: Exception) {
        null
    }
}
