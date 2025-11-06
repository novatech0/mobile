package com.example.agrotech.data.remote.farmer

import com.example.agrotech.domain.farmer.Farmer

data class FarmerDto(
    val id: Long,
    val userId: Long
)

fun FarmerDto.toFarmer() = Farmer(id, userId)