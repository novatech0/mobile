package com.example.agrotech.data.remote.appointment

import com.example.agrotech.domain.appointment.Review

data class ReviewDto(
    val id: Long,
    val advisorId: Long,
    val farmerId: Long,
    val comment: String,
    val rating: Int
)

fun ReviewDto.toReview() = Review(id, advisorId, farmerId, comment, rating)