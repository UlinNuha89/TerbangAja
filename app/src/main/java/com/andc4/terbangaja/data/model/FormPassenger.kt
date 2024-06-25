package com.andc4.terbangaja.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FormPassenger(
    var title: String,
    var fullName: String,
    var familyName: String,
    var birthDate: String,
    var citizenship: String,
    var identityNumber: String,
    var publisherCountry: String,
    var identityExpireDate: String,
) : Parcelable
