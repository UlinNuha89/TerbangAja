package com.andc4.terbangaja.data.source.network.model.auth.profile

import com.google.gson.annotations.SerializedName

data class ProfileData(
    @SerializedName("user")
    val user: User,
    @SerializedName("token")
    val token: String,
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val photo: String?,
    val phone: Int,
    val otp: String,
    val otpExp: String,
    val isVerified: Boolean,
    val updatedAt: String,
    val createdAt: String,
    val deletedAt: String?,
)
