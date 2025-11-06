package com.example.agrotech.domain.authentication

data class AuthenticationResponse(
    val id: Long,
    val username: String,
    val token: String
)