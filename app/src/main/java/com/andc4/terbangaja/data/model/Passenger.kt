package com.andc4.terbangaja.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Passenger(
    var adult: Int,
    var children: Int,
    var baby: Int,
) : Parcelable
