package com.example.agrotech.domain.appointment

data class Appointment(
    val id: Long,
    val farmerId: Long,
    val availableDateId: Long,
    val message: String,
    val status: String,
    val meetingUrl: String
)
