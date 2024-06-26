package com.andc4.terbangaja.data.source.network.model.history

import com.google.gson.annotations.SerializedName

data class BookingData(
    @SerializedName("results")
    val results: List<BookingHistory>?,
    @SerializedName("totalPage")
    val totalPage: Int?,
)

data class BookingHistory(
    @SerializedName("adultCount")
    val adultCount: Int?,
    @SerializedName("babyCount")
    val babyCount: Int?,
    @SerializedName("BookingPassengers")
    val bookingPassengers: List<BookingPassenger>,
    @SerializedName("BookingSeats")
    val bookingSeats: List<BookingSeat>,
    @SerializedName("childCount")
    val childCount: Int?,
    @SerializedName("code")
    val code: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("departure_flight_id")
    val departureFlightId: Int,
    @SerializedName("departureFlight_respon")
    val departureFlightRespon: Flight,
    @SerializedName("id")
    val id: Int,
    @SerializedName("order_date")
    val orderDate: String,
    @SerializedName("Payment")
    val payment: Payment?,
    @SerializedName("price_amount")
    val priceAmount: Int,
    @SerializedName("return_flight_id")
    val returnFlightId: Int?,
    @SerializedName("returnFlight_respon")
    val returnFlightRespon: Flight?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("user_id")
    val userId: Int,
)

data class Payment(
    @SerializedName("booking_code")
    val bookingCode: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("expired_at")
    val expiredAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("redirect_url")
    val redirectUrl: String,
    @SerializedName("start_at")
    val startAt: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("total_price")
    val totalPrice: Int,
    @SerializedName("updatedAt")
    val updatedAt: String?,
)

data class DepartureFlightRespon(
    @SerializedName("Airline")
    val airline: Airline?,
    @SerializedName("airline_id")
    val airlineId: Int?,
    @SerializedName("arrivalAirport")
    val arrivalAirport: Int?,
    @SerializedName("arrivalAirport_respon")
    val arrivalAirportRespon: Airport?,
    @SerializedName("arrivalTime")
    val arrivalTime: String?,
    @SerializedName("businessPrice")
    val businessPrice: Int?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("deletedAt")
    val deletedAt: Any?,
    @SerializedName("departureAirport")
    val departureAirport: Int?,
    @SerializedName("departureAirport_respon")
    val departureAirportRespon: Airport?,
    @SerializedName("departureTime")
    val departureTime: String?,
    @SerializedName("discount")
    val discount: Int?,
    @SerializedName("economyPrice")
    val economyPrice: Int?,
    @SerializedName("firstClassPrice")
    val firstClassPrice: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("numberOfBusinessSeatsLeft")
    val numberOfBusinessSeatsLeft: Int?,
    @SerializedName("numberOfEconomySeatsLeft")
    val numberOfEconomySeatsLeft: Int?,
    @SerializedName("numberOfFirstClassSeatsLeft")
    val numberOfFirstClassSeatsLeft: Int?,
    @SerializedName("numberOfPremiumSeatsLeft")
    val numberOfPremiumSeatsLeft: Int?,
    @SerializedName("premiumPrice")
    val premiumPrice: Int?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
)

data class Flight(
    @SerializedName("Airline")
    val airline: Airline,
    @SerializedName("airline_id")
    val airlineId: Int,
    @SerializedName("arrivalAirport")
    val arrivalAirport: Int,
    @SerializedName("arrivalAirport_respon")
    val arrivalAirportRespon: Airport,
    @SerializedName("arrivalTime")
    val arrivalTime: String,
    @SerializedName("businessPrice")
    val businessPrice: Int?,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String,
    @SerializedName("departureAirport")
    val departureAirport: Int,
    @SerializedName("departureAirport_respon")
    val departureAirportRespon: Airport,
    @SerializedName("departureTime")
    val departureTime: String,
    @SerializedName("discount")
    val discount: Int?,
    @SerializedName("economyPrice")
    val economyPrice: Int?,
    @SerializedName("firstClassPrice")
    val firstClassPrice: Int?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("numberOfBusinessSeatsLeft")
    val numberOfBusinessSeatsLeft: Int,
    @SerializedName("numberOfEconomySeatsLeft")
    val numberOfEconomySeatsLeft: Int,
    @SerializedName("numberOfFirstClassSeatsLeft")
    val numberOfFirstClassSeatsLeft: Int,
    @SerializedName("numberOfPremiumSeatsLeft")
    val numberOfPremiumSeatsLeft: Int,
    @SerializedName("premiumPrice")
    val premiumPrice: Int?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
)

data class Airport(
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imgUrl")
    val imgUrl: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("updatedAt")
    val updatedAt: String?,
)

data class Airline(
    @SerializedName("baggage")
    val baggage: Int,
    @SerializedName("cabinBaggage")
    val cabinBaggage: Int,
    @SerializedName("code")
    val code: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("updatedAt")
    val updatedAt: String?,
)

data class BookingPassenger(
    @SerializedName("booking_code")
    val bookingCode: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("Passenger")
    val passenger: Passenger?,
    @SerializedName("passenger_id")
    val passengerId: Int?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
)

data class Passenger(
    @SerializedName("born_date")
    val bornDate: String,
    @SerializedName("citizenship")
    val citizenship: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("identity_expire_date")
    val identityExpireDate: String?,
    @SerializedName("identity_number")
    val identityNumber: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("publisher_country")
    val publisherCountry: String,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("user_id")
    val userId: Int,
)

data class BookingSeat(
    @SerializedName("booking_code")
    val bookingCode: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("Seat")
    val seat: Seat?,
    @SerializedName("seat_id")
    val seatId: Int?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
)

data class Seat(
    @SerializedName("airline_id")
    val airlineId: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("seat_class")
    val seatClass: String,
    @SerializedName("seat_number")
    val seatNumber: String,
    @SerializedName("updatedAt")
    val updatedAt: String?,
)
