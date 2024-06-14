package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.Login
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.auth.login.LoginData

fun BaseResponse<LoginData>.toLogin() =
    if (this.data == null) {
        Login(
            user = null,
            message = this.message,
            token = "",
        )
    } else {
        Login(
            user = this.data.user.toUserModel(),
            message = this.message,
            token = this.data.token,
        )
    }
