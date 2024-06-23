package com.andc4.terbangaja.data.source.network.model.data

import com.google.gson.annotations.SerializedName

data class FlightsTicket<T>(
    @SerializedName("departure_results")
    val departureFlight: T?,
    @SerializedName("return_results")
    val returnFlight: T?,
)
