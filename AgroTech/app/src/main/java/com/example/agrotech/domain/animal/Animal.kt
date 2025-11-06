package com.example.agrotech.domain.animal

import com.example.agrotech.data.remote.animal.AnimalDto

class Animal (
    val id: Long,
    val enclosureId: Long,
    val name: String,
    val age: Int,
    val species: String,
    val breed: String,
    val gender: Boolean,
    val weight: Float,
    val health: String
)

fun Animal.toAnimalDto() = AnimalDto(
    id = id,
    enclosureId = enclosureId,
    name = name,
    age = age,
    species = species,
    breed = breed,
    gender = gender,
    weight = weight,
    health = health
)