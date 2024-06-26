package com.andc4.terbangaja.data.source.network.model.auth.profile

import com.google.gson.annotations.SerializedName

data class ProfileData(
    @SerializedName("user")
    val user: Profile,
    @SerializedName("token")
    val token: String,
)

data class Profile(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("photo")
    val photo: String?,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("otp")
    val otp: String,
    @SerializedName("otpExp")
    val otpExp: String,
    @SerializedName("isVerified")
    val isVerified: Boolean,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String?,
)
