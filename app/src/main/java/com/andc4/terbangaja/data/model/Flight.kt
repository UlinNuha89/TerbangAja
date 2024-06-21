package com.andc4.terbangaja.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Flight(
    val id: Int,
    val airlineId: Int,
    val departureAirportId: Int,
    val arrivalAirportId: Int,
    val discount: Int,
    val economyPrice: Long,
    val premiumPrice: Long,
    val businessPrice: Long,
    val firstClassPrice: Long,
    val numberOfEconomySeatsLeft: Int,
    val numberOfPremiumSeatsLeft: Int,
    val numberOfBusinessSeatsLeft: Int,
    val numberOfFirstClassSeatsLeft: Int,
    val departureTime: LocalDateTime,
    val arrivalTime: LocalDateTime,
    val airportDeparture: Airport,
    val airportArrival: Airport,
    val airline: Airline,
    val imgDestination: String,
) : Parcelable
