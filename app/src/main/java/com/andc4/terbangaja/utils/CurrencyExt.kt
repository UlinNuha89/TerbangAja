package com.andc4.terbangaja.utils

import java.text.NumberFormat
import java.util.Locale

fun Long?.longToCurrency(
    language: String,
    country: String,
): String? {
    return try {
        val localeID = Locale(language, country)
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.format(this).toString()
    } catch (e: Exception) {
        null
    }
}

fun Long?.toIndonesianFormat() = this.longToCurrency("in", "ID")

fun Int?.IntToCurrency(
    language: String,
    country: String,
): String? {
    return try {
        val localeID = Locale(language, country)
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.format(this).toString()
    } catch (e: Exception) {
        null
    }
}

fun Int?.toIndonesianFormat() = this.IntToCurrency("in", "ID")
