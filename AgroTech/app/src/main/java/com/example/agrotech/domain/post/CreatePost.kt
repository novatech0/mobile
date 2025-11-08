package com.example.agrotech.domain.post

import java.io.File

data class CreatePost(
    val advisorId: Long,
    val title: String,
    val description: String,
    val image: File
)
