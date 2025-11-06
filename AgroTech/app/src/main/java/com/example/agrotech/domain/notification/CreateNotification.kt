package com.example.agrotech.domain.notification

data class CreateNotification(
    val userId: Long,
    val title: String,
    val message: String,
    val sendAt: String
)