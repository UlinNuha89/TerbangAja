package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.*
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.history.*
import com.andc4.terbangaja.data.source.network.model.history.Airline
import com.andc4.terbangaja.data.source.network.model.history.Airport
import com.andc4.terbangaja.data.source.network.model.history.Flight
import com.andc4.terbangaja.data.source.network.model.history.Passenger

fun BaseResponse<BookingData>.toBaseBookingDataModel(): BaseBookingDataModel =
    BaseBookingDataModel(
        message = this.message,
        data = this.data?.toDataModel() ?: BookingDataModel(0, listOf()),
    )

fun BookingData.toDataModel(): BookingDataModel =
    BookingDataModel(
        totalPage = this.totalPage,
        results = this.results?.map { it.toBookingHistoryModel() },
    )

fun BookingHistory.toBookingHistoryModel(): BookingHistoryModel =
    BookingHistoryModel(
        id = this.id,
        userId = this.userId,
        departureFlightId = this.departureFlightId,
        returnFlightId = this.returnFlightId,
        orderDate = this.orderDate,
        priceAmount = this.priceAmount,
        code = this.code,
        adultCount = this.adultCount,
        childCount = this.childCount,
        babyCount = this.babyCount,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt,
        payment = this.payment?.toPaymentModel(),
        departureFlight = this.departureFlightRespon.toFlightModel(),
        returnFlight = this.returnFlightRespon?.toReturnFlightModel(),
        bookingPassengers = this.bookingPassengers.map { it.toBookingPassengerModel() },
        bookingSeats = this.bookingSeats.map { it.toBookingSeatModel() },
    )

fun Payment.toPaymentModel() =
    PaymentModel(
        id = this.id,
        bookingCode = this.bookingCode,
        totalPrice = this.totalPrice,
        status = this.status,
        token = this.token,
        redirectUrl = this.redirectUrl,
        startAt = this.startAt,
        expiredAt = this.expiredAt,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt,
    )

fun Flight.toFlightModel() =
    FlightModel(
        id = this.id,
        airlineId = this.airlineId,
        departureAirport = this.departureAirport,
        arrivalAirport = this.arrivalAirport,
        discount = this.discount,
        economyPrice = this.economyPrice,
        premiumPrice = this.premiumPrice,
        businessPrice = this.businessPrice,
        firstClassPrice = this.firstClassPrice,
        numberOfEconomySeatsLeft = this.numberOfEconomySeatsLeft,
        numberOfPremiumSeatsLeft = this.numberOfPremiumSeatsLeft,
        numberOfBusinessSeatsLeft = this.numberOfBusinessSeatsLeft,
        numberOfFirstClassSeatsLeft = this.numberOfFirstClassSeatsLeft,
        departureTime = this.departureTime,
        arrivalTime = this.arrivalTime,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt,
        departureAirportData = this.departureAirportRespon.toAirportModel(),
        arrivalAirportData = this.arrivalAirportRespon.toAirportModel(),
        airline = this.airline.toAirlineModel(),
    )

fun Flight?.toReturnFlightModel() =
    this?.let {
        FlightModel(
            id = it.id,
            airlineId = this.airlineId,
            departureAirport = this.departureAirport,
            arrivalAirport = this.arrivalAirport,
            discount = this.discount,
            economyPrice = this.economyPrice,
            premiumPrice = this.premiumPrice,
            businessPrice = this.businessPrice,
            firstClassPrice = this.firstClassPrice,
            numberOfEconomySeatsLeft = this.numberOfEconomySeatsLeft,
            numberOfPremiumSeatsLeft = this.numberOfPremiumSeatsLeft,
            numberOfBusinessSeatsLeft = this.numberOfBusinessSeatsLeft,
            numberOfFirstClassSeatsLeft = this.numberOfFirstClassSeatsLeft,
            departureTime = this.departureTime,
            arrivalTime = this.arrivalTime,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deletedAt = this.deletedAt,
            departureAirportData = this.departureAirportRespon.toAirportModel(),
            arrivalAirportData = this.arrivalAirportRespon.toAirportModel(),
            airline = this.airline.toAirlineModel(),
        )
    }

fun Airport.toAirportModel() =
    AirportModel(
        id = this.id,
        name = this.name,
        city = this.city,
        country = this.country,
        imgUrl = this.imgUrl,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt,
    )

fun Airline.toAirlineModel() =
    AirlineModel(
        id = this.id,
        name = this.name,
        code = this.code,
        imgUrl = this.imgUrl,
        baggage = this.baggage,
        cabinBaggage = this.cabinBaggage,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt,
    )

fun BookingPassenger.toBookingPassengerModel() =
    BookingPassengerModel(
        id = this.id,
        bookingCode = this.bookingCode,
        passengerId = this.passengerId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt,
        passenger = this.passenger?.toPassengerModel(),
    )

fun Passenger.toPassengerModel() =
    PassengerModel(
        id = this.id,
        userId = this.userId,
        title = this.title,
        name = this.name,
        bornDate = this.bornDate,
        citizenship = this.citizenship,
        identityNumber = this.identityNumber,
        publisherCountry = this.publisherCountry,
        identityExpireDate = this.identityExpireDate,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt,
    )

fun BookingSeat.toBookingSeatModel() =
    BookingSeatModel(
        id = this.id,
        bookingCode = this.bookingCode,
        seatId = this.seatId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt,
        seat = this.seat?.toSeatModel(),
    )

fun Seat.toSeatModel() =
    SeatModel(
        id = this.id,
        seatNumber = this.seatNumber,
        seatClass = this.seatClass,
        airlineId = this.airlineId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt,
    )

fun BookingHistoryModel?.toSearchHistory() =
    SearchHistory(
        id = this?.id ?: 0,
        userId = this?.userId ?: 0,
        code = this?.code.orEmpty(),
        departure = this?.departureFlight?.departureAirportData?.city + "->" + this?.departureFlight?.arrivalAirportData?.city.orEmpty(),
        returnflight =
            if (this?.returnFlight != null) {
                this.returnFlight.departureAirportData.city + "->" + this.returnFlight.arrivalAirportData.city
            } else {
                null
            },
    )

fun List<BookingHistoryModel>?.toSearchHistoryBookingList() = this?.map { it.toSearchHistory() } ?: listOf()

fun List<BookingPassengerModel>?.toListPassenger() =
    listOf(
        this?.map { it.passenger },
    ) ?: listOf()
