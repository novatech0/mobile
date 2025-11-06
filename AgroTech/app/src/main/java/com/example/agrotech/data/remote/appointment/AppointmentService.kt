package com.example.agrotech.data.remote.appointment

import com.example.agrotech.domain.appointment.CreateAppointment
import com.example.agrotech.domain.appointment.UpdateAppointment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AppointmentService {
    @GET("appointments/{id}")
    suspend fun getAppointment(@Path("id") id: Long, @Header("Authorization") token: String): Response<AppointmentDto>

    @GET("appointments")
    suspend fun getAllAppointments(@Header("Authorization") token: String): Response<List<AppointmentDto>>

    @GET("appointments")
    suspend fun getAppointmentsByFarmer(@Query("farmerId") farmerId: Long, @Header("Authorization") token: String): Response<List<AppointmentDto>>

    @GET("appointments")
    suspend fun getAppointmentsByAdvisor(@Query("advisorId") advisorId: Long, @Header("Authorization") token: String): Response<List<AppointmentDto>>

    @GET("appointments")
    suspend fun getAppointmentsByAdvisorAndFarmer(@Query("advisorId") advisorId: Long, @Query("farmerId") farmerId: Long, @Header("Authorization") token: String): Response<List<AppointmentDto>>

    @POST("appointments")
    suspend fun createAppointment(@Header("Authorization") token: String, @Body appointment: CreateAppointment): Response<AppointmentDto>

    @PUT("appointments/{id}")
    suspend fun updateAppointment(@Path("id") id: Long, @Header("Authorization") token: String, @Body appointment: UpdateAppointment): Response<AppointmentDto>

    @DELETE("appointments/{id}")
    suspend fun deleteAppointment(@Path("id") id: Long, @Header("Authorization") token: String): Response<Unit>
}