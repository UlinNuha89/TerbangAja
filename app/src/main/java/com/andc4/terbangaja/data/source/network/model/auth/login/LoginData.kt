package com.andc4.terbangaja.data.source.network.model.auth.login

import com.andc4.terbangaja.data.source.network.model.auth.register.UserResponse
import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("user")
    val user: UserResponse,
    @SerializedName("token")
    val token: String,
)
