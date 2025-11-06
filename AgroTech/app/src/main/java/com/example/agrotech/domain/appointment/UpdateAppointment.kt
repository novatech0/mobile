package com.example.agrotech.domain.appointment

data class UpdateAppointment(
    val message: String,
    val status: String,
    val scheduledDate: String,
    val startTime: String,
    val endTime: String
)