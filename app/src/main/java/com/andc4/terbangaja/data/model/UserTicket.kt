package com.andc4.terbangaja.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserTicket(
    val departureFlight: Flight,
    var returnFlight: Flight?,
    var departureSeats: List<Seats>?,
    var returnSeats: List<Seats>?,
    val seatClass: SeatClass,
    val passenger: Passenger,
) : Parcelable
