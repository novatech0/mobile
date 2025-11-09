package com.example.agrotech.domain.appointment

data class CreateAvailableDate(
    val advisorId: Long,
    val scheduledDate: String,
    val startTime: String,
    val endTime: String,
)