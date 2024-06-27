package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.FormPassenger
import com.andc4.terbangaja.data.source.network.model.data.dobooking.BookingPassengerPayload
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun List<FormPassenger>?.toPassengerPayload() =
    this?.map {
        BookingPassengerPayload(
            title = it.title.trim(),
            if (it.familyName != "") {
                it.fullName.trim() + " " + it.familyName.trim()
            } else {
                it.fullName.trim()
            },
            bornDate = it.birthDate.toDateFormat(),
            citizenship = it.citizenship.trim(),
            identityNumber = it.identityNumber,
            publisherCountry = it.publisherCountry,
            identityExpireDate = it.identityExpireDate.toDateFormat(),
        )
    }

fun String?.toDateFormat(): String {
    val originalFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")
    val originalDate = LocalDate.parse(this, originalFormatter)
    val desiredFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return originalDate.format(desiredFormatter)
}
