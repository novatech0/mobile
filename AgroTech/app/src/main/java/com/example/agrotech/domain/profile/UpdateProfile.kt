package com.example.agrotech.domain.profile

import java.io.File

data class UpdateProfile(
    val firstName: String,
    val lastName: String,
    val city: String,
    val country: String,
    val birthDate: String,
    val description: String,
    val photo: File?,
    val occupation: String,
    val experience: Int
)
