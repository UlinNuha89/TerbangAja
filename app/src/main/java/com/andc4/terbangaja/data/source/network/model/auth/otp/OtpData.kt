package com.andc4.terbangaja.data.source.network.model.auth.otp

import com.google.gson.annotations.SerializedName

data class OtpData(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("photo")
    var photo: String? = null,
    @SerializedName("phone")
    var phone: String? = null,
    @SerializedName("isVerified")
    var isVerified: Boolean? = null,
)
