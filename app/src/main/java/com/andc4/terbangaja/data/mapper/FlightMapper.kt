package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.source.network.model.BasePaging
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.FlightsData
import com.andc4.terbangaja.data.source.network.model.data.FlightsTicket
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
            numberOfEconomySeatsLeft = it.numberOfEconomySeatsLeft,
            numberOfPremiumSeatsLeft = it.numberOfPremiumSeatsLeft,
            numberOfBusinessSeatsLeft = it.numberOfBusinessSeatsLeft,
            numberOfFirstClassSeatsLeft = it.numberOfFirstClassSeatsLeft,
            departureTime = OffsetDateTime.parse(it.departureTime).toLocalDateTime(),
            arrivalTime = OffsetDateTime.parse(it.arrivalTime).toLocalDateTime(),
            airportDeparture = it.departureAirport_respon.toAirport(),
            airportArrival = it.arrivalAirport_respon.toAirport(),
            airline = it.Airline.toAirline(),
            imgDestination = it.arrivalAirport_respon.imgUrl,
        )
    } ?: listOf()

fun BaseResponse<FlightsTicket<BasePaging<List<FlightsData>>>>.toFlightTicket() =
    this.data?.departureFlight?.result?.map {
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
            numberOfEconomySeatsLeft = it.numberOfEconomySeatsLeft,
            numberOfPremiumSeatsLeft = it.numberOfPremiumSeatsLeft,
            numberOfBusinessSeatsLeft = it.numberOfBusinessSeatsLeft,
            numberOfFirstClassSeatsLeft = it.numberOfFirstClassSeatsLeft,
            departureTime = OffsetDateTime.parse(it.departureTime).toLocalDateTime(),
            arrivalTime = OffsetDateTime.parse(it.arrivalTime).toLocalDateTime(),
            airportDeparture = it.departureAirport_respon.toAirport(),
            airportArrival = it.arrivalAirport_respon.toAirport(),
            airline = it.Airline.toAirline(),
            imgDestination = it.arrivalAirport_respon.imgUrl,
        )
    } ?: listOf()
