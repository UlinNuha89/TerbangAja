package com.andc4.terbangaja.data.source.network.model.auth.forgotpassword

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ForgotPasswordResponse(
    @SerializedName("link")
    val link: String?,
    @SerializedName("message")
    val message: String?,
)
