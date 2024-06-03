package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.Register
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.auth.register.RegisterData

fun BaseResponse<RegisterData>.toRegister() =
    if (this.data == null) {
        Register(
            isVerified = true,
            email = "",
            message = this.message,
            token = "",
        )
    } else {
        Register(
            isVerified = this.data.user.isVerified,
            email = this.data.user.email,
            message = this.message,
            token = this.data.token,
        )
    }
