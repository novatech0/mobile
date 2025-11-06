package com.example.agrotech.domain.appointment

data class CreateReview(
    val advisorId: Long,
    val farmerId: Long,
    val comment: String,
    val rating: Int
)