package com.andc4.terbangaja.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Airline(
    val id: Int,
    val name: String,
    val code: String,
    val baggage: Int,
    val cabinBaggage: Int,
    val imgUrl: String,
) : Parcelable
