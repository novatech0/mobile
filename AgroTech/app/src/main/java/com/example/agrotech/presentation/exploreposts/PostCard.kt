package com.example.agrotech.presentation.exploreposts

data class PostCard (
    val id: Long,
    val title: String,
    val description: String,
    val image: String,
    val advisorId: Long,
    val advisorName: String,
    val advisorPhoto: String
)