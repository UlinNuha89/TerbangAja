package com.andc4.terbangaja.data.source.network.model.auth.register

data class RegisterResponse(
    val message: String,
    val data: RegisterData,
)

data class RegisterData(
    val user: User,
    val token: String,
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val photo: String?,
    val phone: String,
    val otp: String,
    val otpExp: String,
    val isVerified: Boolean,
    val updatedAt: String,
    val createdAt: String,
    val deletedAt: String?,
)
