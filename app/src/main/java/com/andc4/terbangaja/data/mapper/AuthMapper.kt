package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.User
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.auth.profile.ProfileData

fun BaseResponse<ProfileData>.toUser() =
    this.data?.user.let {
        User(
            id = it?.id ?: 0,
            name = it?.name.orEmpty(),
            email = it?.email.orEmpty(),
            photo = it?.photo.orEmpty(),
            phone = it?.phone ?: 0,
        )
    }
