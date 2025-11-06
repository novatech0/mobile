package com.example.agrotech.data.repository.notification

import com.example.agrotech.common.Resource
import com.example.agrotech.data.remote.notification.NotificationDto
import com.example.agrotech.data.remote.notification.NotificationService
import com.example.agrotech.data.remote.notification.toNotification
import com.example.agrotech.domain.notification.CreateNotification
import com.example.agrotech.domain.notification.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationRepository(private val notificationService: NotificationService) {

    suspend fun getNotifications(userId: Long, token: String): Resource<List<Notification>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = notificationService.getNotifications(userId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { notificationsDto ->
                val notifications = notificationsDto.map { it.toNotification() }
                return@withContext Resource.Success(notifications)
            }
            return@withContext Resource.Error(message = "Error al obtener notificaciones")
        }
        else {
            return@withContext Resource.Error(response.message())
        }
    }

    suspend fun createNotification(notification: Notification, token: String): Resource<Notification> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = notificationService.createNotification(bearerToken, CreateNotification(notification.userId, notification.title, notification.message, notification.sendAt))
        if (response.isSuccessful) {
            response.body()?.let { notificationDto: NotificationDto ->
                val notificationCreated = notificationDto.toNotification()
                return@withContext Resource.Success(notificationCreated)
            }
            return@withContext Resource.Error(message = "Error al crear notificación")
        } else {
            return@withContext Resource.Error(response.message())
        }
    }

    suspend fun deleteNotification(notificationId: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = notificationService.deleteNotification(notificationId, bearerToken)
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        else {
            return@withContext Resource.Error(response.message())
        }
    }




}