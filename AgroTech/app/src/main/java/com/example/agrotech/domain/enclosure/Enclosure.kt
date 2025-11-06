package com.example.agrotech.domain.enclosure

import com.example.agrotech.data.remote.enclosure.EnclosureDto

class Enclosure (
    val id: Long,
    val farmerId: Long,
    val name: String,
    val capacity: Int,
    val type: String
)

fun Enclosure.toEnclosureDto() = EnclosureDto(
    id = id,
    farmerId = farmerId,
    name = name,
    capacity = capacity,
    type = type
)