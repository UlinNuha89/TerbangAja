package com.andc4.terbangaja.data.source.network.model.data

import com.google.gson.annotations.SerializedName

data class SeatData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("seat_no")
    val seatNo: String,
    @SerializedName("booked")
    val booked: Boolean,
)
