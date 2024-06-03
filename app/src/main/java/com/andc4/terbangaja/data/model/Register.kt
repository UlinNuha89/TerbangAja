package com.andc4.terbangaja.data.model

data class Register(
    val isVerified: Boolean? = false,
    val email: String?,
    val message: String,
    val token: String,
)
