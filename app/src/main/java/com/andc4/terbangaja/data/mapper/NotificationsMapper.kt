package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.source.network.model.notification.Notification

fun Notification.toNotification() =
    com.andc4.terbangaja.data.model.Notification(
        id = this.id,
        type = this.type,
        title = this.title,
        content = this.content,
        userId = this.userId,
        isRead = this.isRead,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt,
    )

fun List<Notification>.toNotifications(): List<com.andc4.terbangaja.data.model.Notification> {
    return this.map { it.toNotification() }
}
