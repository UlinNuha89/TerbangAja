package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.source.network.model.auth.register.UserResponse

fun UserResponse.toUserModel() =
    com.andc4.terbangaja.data.model.User(
        id = this.id,
        name = this.name,
        email = this.email,
        photo = this.photo,
        phone = this.phone,
        otp = this.otp,
        otpExp = this.otpExp,
        isVerified = this.isVerified,
        updatedAt = this.updatedAt,
        createdAt = this.createdAt,
        deletedAt = this.deletedAt,
    )
