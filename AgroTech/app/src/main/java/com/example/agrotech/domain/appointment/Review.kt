package com.example.agrotech.domain.appointment

data class Review (
    val id: Long,
    val advisorId: Long,
    val farmerId: Long,
    val comment: String,
    val rating: Int
)