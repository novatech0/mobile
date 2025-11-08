package com.example.agrotech.domain.appointment

data class CreateAppointment(
    val farmerId: Long,
    val availableDateId: Long,
    val message: String,
)