package com.andc4.terbangaja.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Ticket(
    val airportFrom: Airport,
    val airportTo: Airport,
    val departureDate: LocalDate,
    val returnDate: LocalDate?,
    val passenger: Passenger,
    val seatClass: SeatClass,
) : Parcelable
