package com.example.agrotech.data.remote.profile

import com.example.agrotech.domain.profile.Profile

data class ProfileDto(
    val birthDate: String,
    val city: String,
    val country: String,
    val description: String,
    val experience: Int,
    val firstName: String,
    val id: Long,
    val lastName: String,
    val occupation: String,
    val photo: String,
    val userId: Long
)

fun ProfileDto.toProfile() = Profile(id, userId, firstName, lastName, city, country, birthDate, description, occupation, photo, experience)