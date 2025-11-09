package com.example.agrotech.data.remote.appointment

import com.example.agrotech.domain.appointment.Appointment

data class AppointmentDto(
    val id: Long,
    val farmerId: Long,
    val availableDateId: Long,
    val message: String,
    val status: String,
    val meetingUrl: String
)

fun AppointmentDto.toAppointment() = Appointment(id, farmerId, availableDateId, message, status, meetingUrl)