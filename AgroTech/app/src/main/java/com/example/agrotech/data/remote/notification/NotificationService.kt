package com.example.agrotech.data.remote.notification

import com.example.agrotech.domain.notification.CreateNotification
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationService {
    @GET("notifications/{userId}/user")
    suspend fun getNotifications(@Path("userId") userId: Long, @Header("Authorization") token: String): Response<List<NotificationDto>>
    @GET("notifications/{id}")
    suspend fun getNotification(@Path("id") id: Long, @Header("Authorization") token: String): Response<NotificationDto>
    @DELETE("notifications/{id}")
    suspend fun deleteNotification(@Path("id") id: Long, @Header("Authorization") token: String): Response<Unit>
    @POST("notifications")
    suspend fun createNotification(@Header("Authorization") token: String, @Body notification: CreateNotification): Response<NotificationDto>
}