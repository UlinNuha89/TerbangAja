package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.source.network.model.BasePaging
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.FlightsData
import java.time.OffsetDateTime

fun BaseResponse<BasePaging<List<FlightsData>>>.toFlight() =
    this.data?.result?.map {
        Flight(
            id = it.id,
            airlineId = it.airlineId,
            departureAirportId = it.departureAirport,
            arrivalAirportId = it.arrivalAirport,
            discount = it.discount,
            economyPrice = it.economyPrice,
            premiumPrice = it.premiumPrice,
            businessPrice = it.businessPrice,
            firstClassPrice = it.firstClassPrice,
            departureTime = OffsetDateTime.parse(it.departureTime).toLocalDateTime(),
            arrivalTime = OffsetDateTime.parse(it.arrivalTime).toLocalDateTime(),
            airportDepartureName = it.departureAirport_respon.city,
            airportArrivalName = it.arrivalAirport_respon.city,
            airlineName = it.Airline.name,
            imgDestination = it.arrivalAirport_respon.imgUrl,
        )
    } ?: listOf()
