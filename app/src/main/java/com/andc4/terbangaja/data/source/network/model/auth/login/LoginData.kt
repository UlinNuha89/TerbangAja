package com.andc4.terbangaja.data.source.network.model.auth.login

import com.andc4.terbangaja.data.source.network.model.auth.register.User

data class LoginData(
    val user: User,
    val token: String,
)
