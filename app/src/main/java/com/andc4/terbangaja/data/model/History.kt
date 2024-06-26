package com.andc4.terbangaja.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class BaseBookingDataModel(
    val message: String,
    val data: BookingDataModel,
)

data class BookingDataModel(
    val totalPage: Int?,
    val results: List<BookingHistoryModel>?,
)

@Parcelize
data class BookingHistoryModel(
    val id: Int,
    val userId: Int,
    val departureFlightId: Int,
    val returnFlightId: Int?,
    val orderDate: String,
    val priceAmount: Int,
    val code: String,
    val adultCount: Int?,
    val childCount: Int?,
    val babyCount: Int?,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
    val payment: PaymentModel?,
    val departureFlight: FlightModel,
    val returnFlight: FlightModel?,
    val bookingPassengers: List<BookingPassengerModel>,
    val bookingSeats: List<BookingSeatModel>,
) : Parcelable

@Parcelize
data class PaymentModel(
    val id: Int,
    val bookingCode: String,
    val totalPrice: Int,
    val status: String,
    val token: String,
    val redirectUrl: String,
    val startAt: String,
    val expiredAt: String,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
) : Parcelable

@Parcelize
data class FlightModel(
    val id: Int,
    val airlineId: Int,
    val departureAirport: Int,
    val arrivalAirport: Int,
    val discount: Int?,
    val economyPrice: Int?,
    val premiumPrice: Int?,
    val businessPrice: Int?,
    val firstClassPrice: Int?,
    val numberOfEconomySeatsLeft: Int,
    val numberOfPremiumSeatsLeft: Int,
    val numberOfBusinessSeatsLeft: Int,
    val numberOfFirstClassSeatsLeft: Int,
    val departureTime: String,
    val arrivalTime: String,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
    val departureAirportData: AirportModel,
    val arrivalAirportData: AirportModel,
    val airline: AirlineModel,
) : Parcelable

@Parcelize
data class AirportModel(
    val id: Int,
    val name: String,
    val city: String,
    val country: String,
    val imgUrl: String?,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
) : Parcelable

@Parcelize
data class AirlineModel(
    val id: Int,
    val name: String,
    val code: String,
    val imgUrl: String,
    val baggage: Int,
    val cabinBaggage: Int,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
) : Parcelable

@Parcelize
data class BookingPassengerModel(
    val id: Int,
    val bookingCode: String,
    val passengerId: Int?,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
    val passenger: PassengerModel?,
) : Parcelable

@Parcelize
data class PassengerModel(
    val id: Int,
    val userId: Int,
    val title: String?,
    val name: String,
    val bornDate: String,
    val citizenship: String,
    val identityNumber: String?,
    val publisherCountry: String,
    val identityExpireDate: String?,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
) : Parcelable

@Parcelize
data class BookingSeatModel(
    val id: Int,
    val bookingCode: String,
    val seatId: Int?,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
    val seat: SeatModel?,
) : Parcelable

@Parcelize
data class SeatModel(
    val id: Int,
    val seatNumber: String,
    val seatClass: String,
    val airlineId: Int,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
) : Parcelable
