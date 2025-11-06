package com.example.agrotech.data.remote.post

import com.example.agrotech.domain.post.Post

data class PostDto(
    val id: Long,
    val advisorId: Long,
    val title: String,
    val description: String,
    val image: String
)

fun PostDto.toPost() = Post(id, advisorId, title, description, image)