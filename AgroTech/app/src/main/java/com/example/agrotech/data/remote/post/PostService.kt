package com.example.agrotech.data.remote.post

import com.example.agrotech.domain.post.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PostService {
    @Multipart
    @POST("posts")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Part("advisorId") advisorId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<PostDto>

    @Multipart
    @PUT("posts/{id}")
    suspend fun updatePost(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<PostDto>

    @GET("posts")
    suspend fun getPosts(@Header("Authorization") token: String): Response<List<PostDto>>

    @GET("posts")
    suspend fun getPostsByAdvisorId(@Header("Authorization") token: String, @Query("advisorId") advisorId: Long): Response<List<PostDto>>

    @GET("posts/{id}")
    suspend fun getPost(@Header("Authorization") token: String, @Path("id") id: Long): Response<PostDto>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Header("Authorization") token: String, @Path("id") id: Long): Response<Unit>
}