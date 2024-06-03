package com.andc4.terbangaja.data.source.network.model.auth.otp

import com.google.gson.annotations.SerializedName

data class OtpRequestPayload(
    @SerializedName("email")
    val email: String,
    @SerializedName("otp")
    val otp: String,
)
