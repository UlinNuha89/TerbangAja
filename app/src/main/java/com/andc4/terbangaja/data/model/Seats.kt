package com.andc4.terbangaja.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Seats(
    val id: Int,
    val seatNo: String,
    val booked: Boolean,
) : Parcelable
