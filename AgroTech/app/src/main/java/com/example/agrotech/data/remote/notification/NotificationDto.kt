package com.example.agrotech.data.remote.notification

import com.example.agrotech.domain.notification.Notification

data class NotificationDto(
    val id: Long,
    val userId: Long,
    val title: String,
    val message: String,
    val sendAt: String
)

fun NotificationDto.toNotification() = Notification(id, userId, title, message, sendAt)