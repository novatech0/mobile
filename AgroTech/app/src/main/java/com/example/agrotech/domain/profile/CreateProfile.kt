package com.example.agrotech.domain.profile

data class CreateProfile(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val city: String,
    val country: String,
    val birthDate: String,
    val description: String,
    val occupation: String,
    val photo: String,
    val experience: Int
)
