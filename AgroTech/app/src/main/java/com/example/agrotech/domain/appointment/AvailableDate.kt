package com.example.agrotech.domain.appointment

data class AvailableDate (
    val id : Long,
    val advisorId: Long,
    val scheduledDate: String,
    val startTime: String,
    val endTime: String,
    val status: String,
)