package com.example.agrotech.data.remote.advisor

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AdvisorService {
    @GET("advisors/{userId}/user")
    suspend fun getAdvisor(@Path("userId") userId: Long, @Header("Authorization") token: String): Response<AdvisorDto>

    @GET("advisors/{id}")
    suspend fun getAdvisorByAdvisorId(@Path("id") id: Long, @Header("Authorization") token: String): Response<AdvisorDto>
}