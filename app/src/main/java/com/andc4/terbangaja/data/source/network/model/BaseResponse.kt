package com.andc4.terbangaja.data.source.network.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BaseResponse<T>(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T?,
)
