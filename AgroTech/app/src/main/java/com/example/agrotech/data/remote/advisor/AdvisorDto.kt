package com.example.agrotech.data.remote.advisor

import com.example.agrotech.domain.advisor.Advisor

data class AdvisorDto(
    val id: Long,
    val userId: Long,
    val rating: Double,
)

fun AdvisorDto.toAdvisor() = Advisor(id, userId, rating)