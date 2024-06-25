package com.andc4.terbangaja.data.source.network.model.data.dobooking

import com.google.gson.annotations.SerializedName

data class BookingRequestPayload(
    @SerializedName("departure_flight_id")
    val departureFlightId: Int,
    @SerializedName("return_flight_id")
    val returnFlightId: Int? = null,
    @SerializedName("price_amount")
    val priceAmount: Int,
    @SerializedName("seats_id")
    val seatsId: List<Int>,
    @SerializedName("seat_class")
    val seatClass: String,
    @SerializedName("passengers")
    val passengers: List<BookingPassengerPayload>,
    @SerializedName("adultCount")
    val adultCount: Int,
    @SerializedName("childCount")
    val childCount: Int,
    @SerializedName("babyCount")
    val babyCount: Int,
)
