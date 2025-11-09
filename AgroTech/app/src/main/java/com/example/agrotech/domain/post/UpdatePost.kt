package com.example.agrotech.domain.post

import java.io.File

data class UpdatePost(
    val id: Long,
    val title: String,
    val description: String,
    val image: File? = null
)
