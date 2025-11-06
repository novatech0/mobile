package com.example.agrotech.data.remote.farmer

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface FarmerService {
    @GET("farmers/{id}")
    suspend fun getFarmer(@Path("id") id: Long, @Header("Authorization") token: String): Response<FarmerDto>
    @GET("farmers/{userId}/user")
    suspend fun getFarmerByUserId(@Path("userId") userId: Long, @Header("Authorization") token: String): Response<FarmerDto>
}