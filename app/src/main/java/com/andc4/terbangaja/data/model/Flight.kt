package com.andc4.terbangaja.data.model

import java.time.LocalDateTime

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
    val departureTime: LocalDateTime,
    val arrivalTime: LocalDateTime,
    val airportDepartureName: String,
    val airportArrivalName: String,
    val airlineName: String,
    val imgDestination: String,
)
