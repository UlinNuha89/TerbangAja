package com.andc4.terbangaja.data.source.network.model

data class BaseResponse<T>(
    val message: String,
    val data: T,
)
