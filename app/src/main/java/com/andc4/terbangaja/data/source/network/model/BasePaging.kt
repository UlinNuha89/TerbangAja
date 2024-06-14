package com.andc4.terbangaja.data.source.network.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BasePaging<T>(
    @SerializedName("next")
    val next: PageData?,
    @SerializedName("previous")
    val previous: PageData?,
    @SerializedName("results")
    val result: T?,
)
