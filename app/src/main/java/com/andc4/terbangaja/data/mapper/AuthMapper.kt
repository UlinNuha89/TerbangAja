package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.User
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.auth.profile.Profile
import com.andc4.terbangaja.data.source.network.model.auth.profile.ProfileData

fun BaseResponse<ProfileData>.toUser() =
    this.data?.user.let {
        User(
            id = it?.id.orEmpty(),
            name = it?.name.orEmpty(),
            email = it?.email.orEmpty(),
            photo = it?.photo.orEmpty(),
            phone = it?.phone.orEmpty(),
        )
    }

fun BaseResponse<Profile>.toProfile() =
    User(
        id = this.data?.id.orEmpty(),
        name = this.data?.name.orEmpty(),
        email = this.data?.email.orEmpty(),
        photo = this.data?.photo.orEmpty(),
        phone = this.data?.phone.orEmpty(),
    )
