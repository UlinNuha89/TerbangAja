package com.andc4.terbangaja.data.source.network.model.auth.login

import com.andc4.terbangaja.data.source.network.model.auth.register.User
import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("user")
    val user: User,
    @SerializedName("token")
    val token: String,
)
