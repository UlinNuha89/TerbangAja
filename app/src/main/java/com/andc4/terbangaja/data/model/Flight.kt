package com.andc4.terbangaja.data.model

import java.time.LocalDateTime

data class Flight(
    val id: Int,
    val airlineId: Int,
    val departureAirport: Int,
    val arrivalAirport: Int,
    val discount: Int,
    val economyPrice: Long,
    val premiumPrice: Long,
    val businessPrice: Long,
    val firstClassPrice: Long,
    val departureTime: LocalDateTime,
    val arrivalTime: LocalDateTime,
)
