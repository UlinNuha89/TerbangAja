package com.andc4.terbangaja.data.source.network.model.data.dobooking

import com.google.gson.annotations.SerializedName

data class BookingResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("redirect_url")
    val redirectUrl: String,
)
