package com.andc4.terbangaja.data.source.network.model.data.dobooking

import com.google.gson.annotations.SerializedName

data class BookingPassengerPayload(
    @SerializedName("title")
    val title: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("born_date")
    val bornDate: String,
    @SerializedName("citizenship")
    val citizenship: String,
    @SerializedName("identity_number")
    val identityNumber: String,
    @SerializedName("publisher_country")
    val publisherCountry: String,
    @SerializedName("identity_expire_date")
    val identityExpireDate: String,
)
