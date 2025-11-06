package com.example.agrotech.data.remote.enclosure

import com.example.agrotech.domain.enclosure.Enclosure

data class EnclosureDto(
    val id: Long,
    val farmerId: Long,
    val name: String,
    val capacity: Int,
    val type: String
)

fun EnclosureDto.toEnclosure() = Enclosure(
    id = id,
    farmerId = farmerId,
    name = name,
    capacity = capacity,
    type = type
)