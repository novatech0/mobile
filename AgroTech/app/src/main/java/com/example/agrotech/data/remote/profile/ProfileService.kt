package com.example.agrotech.data.remote.profile

import com.example.agrotech.domain.profile.CreateProfile
import com.example.agrotech.domain.profile.UpdateProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileService {
    @POST("profiles")
    suspend fun createProfile(@Header("Authorization") token: String, @Body profile: CreateProfile): Response<ProfileDto>

    @GET("profiles/{userId}/user")
    suspend fun getProfile(@Path("userId") userId: Long, @Header("Authorization") token: String): Response<ProfileDto>

    @GET("profiles/advisors")
    suspend fun getAdvisors(@Header("Authorization") token: String): Response<List<ProfileDto>>

    @PUT("profiles/{id}")
    suspend fun updateProfile(@Path("id") id: Long, @Header("Authorization") token: String, @Body profile: UpdateProfile): Response<ProfileDto>
}