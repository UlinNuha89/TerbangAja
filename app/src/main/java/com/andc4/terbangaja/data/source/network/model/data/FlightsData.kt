package com.andc4.terbangaja.data.source.network.model.data

data class FlightsData(
    val id: Int,
    val airlineId: Int,
    val departureAirport: Int,
    val arrivalAirport: Int,
    val discount: Int,
    val economyPrice: Long,
    val premiumPrice: Long,
    val businessPrice: Long,
    val firstClassPrice: Long,
    val numberOfEconomySeatsLeft: Int,
    val numberOfPremiumSeatsLeft: Int,
    val numberOfBusinessSeatsLeft: Int,
    val numberOfFirstClassSeatsLeft: Int,
    val departureTime: String,
    val arrivalTime: String,
)
