package com.andc4.terbangaja.data.source.network.model.data

import com.google.gson.annotations.SerializedName

data class FlightsTicket(
    @SerializedName("departure_flight")
    val departureFlight: List<FlightsData>?,
    @SerializedName("return_flight")
    val returnFlight: List<FlightsData>?,
)
