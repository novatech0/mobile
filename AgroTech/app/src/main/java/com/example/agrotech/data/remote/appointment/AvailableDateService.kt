package com.example.agrotech.data.remote.appointment

import com.example.agrotech.domain.appointment.AvailableDate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AvailableDateService {
    @GET("available_dates")
    suspend fun getAvailableDates(@Header("Authorization") token: String): Response<List<AvailableDateDto>>

    @POST("available_dates")
    suspend fun createAvailableDate(@Header("Authorization") token: String, @Body availableDate: AvailableDate): Response<AvailableDateDto>

    @DELETE("available_dates/{availableDateId}")
    suspend fun deleteAvailableDate(
        @Path("availableDateId") availableDateId: Long,
        @Header("Authorization") token: String): Response<Unit>
}