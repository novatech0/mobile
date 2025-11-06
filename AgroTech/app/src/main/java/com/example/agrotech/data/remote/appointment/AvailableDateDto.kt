package com.example.agrotech.data.remote.appointment

import com.example.agrotech.domain.appointment.AvailableDate

data class AvailableDateDto(
    val id: Long,
    val advisorId: Long,
    val scheduledDate: String,
    val startTime: String,
    val endTime: String,
)

fun AvailableDateDto.toAvailableDate() = AvailableDate(id, advisorId, scheduledDate, startTime, endTime)