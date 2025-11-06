package com.example.agrotech.domain.authentication

data class SignUpResponse(
    val id : Long,
    val username : String,
    val roles : List<String>
)
