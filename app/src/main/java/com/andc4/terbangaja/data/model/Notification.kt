package com.andc4.terbangaja.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val id: Int,
    val type: String,
    val title: String,
    val content: String,
    val userId: Int,
    val isRead: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
) : Parcelable
