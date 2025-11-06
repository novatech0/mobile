package com.example.agrotech.presentation.advisordetail

data class AdvisorDetail (
    val id: Long,
    val name: String,
    val description: String,
    val occupation: String,
    val experience: Int,
    val rating: Double,
    val link: String
)