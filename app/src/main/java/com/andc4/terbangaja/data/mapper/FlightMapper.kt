package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.FlightsData
import java.time.OffsetDateTime

fun BaseResponse<List<FlightsData>>.toFlight() =
    this.data?.map {
        Flight(
            id = it.id,
            airlineId = it.airlineId,
            departureAirport = it.departureAirport,
            arrivalAirport = it.arrivalAirport,
            discount = it.discount,
            economyPrice = it.economyPrice,
            premiumPrice = it.premiumPrice,
            businessPrice = it.businessPrice,
            firstClassPrice = it.firstClassPrice,
            departureTime = OffsetDateTime.parse(it.departureTime).toLocalDateTime(),
            arrivalTime = OffsetDateTime.parse(it.arrivalTime).toLocalDateTime(),
        )
    } ?: listOf()
