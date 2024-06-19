package com.andc4.terbangaja.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Airport(
    val id: Int,
    val name: String,
    val city: String,
    val country: String,
    val imgUrl: String,
) : Parcelable
