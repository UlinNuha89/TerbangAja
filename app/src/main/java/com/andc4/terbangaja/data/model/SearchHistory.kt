package com.andc4.terbangaja.data.model

data class SearchHistory(
    val id: Int,
    val userId: Int,
    val code: String,
    val departure: String,
    val returnflight: String?,
)
