package com.example.agrotech.domain.post

data class Post(
    val id: Long,
    val advisorId: Long,
    val title: String,
    val description: String,
    val image: String
)