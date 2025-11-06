package com.example.agrotech.domain.notification

data class Notification (
    val id: Long,
    val userId: Long,
    val title: String,
    val message: String,
    val sendAt: String
)