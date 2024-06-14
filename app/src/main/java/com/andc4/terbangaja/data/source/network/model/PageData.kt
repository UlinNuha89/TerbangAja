package com.andc4.terbangaja.data.source.network.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PageData(
    @SerializedName("page")
    val page: Int,
    @SerializedName("limit")
    val limit: Int,
)
