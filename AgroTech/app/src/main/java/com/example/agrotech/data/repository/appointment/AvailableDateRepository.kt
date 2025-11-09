package com.example.agrotech.data.repository.appointment

import com.example.agrotech.common.Resource
import com.example.agrotech.data.remote.appointment.AvailableDateService
import com.example.agrotech.data.remote.appointment.toAvailableDate
import com.example.agrotech.domain.appointment.AvailableDate
import com.example.agrotech.domain.appointment.CreateAvailableDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AvailableDateRepository @Inject constructor(private val availableDateService: AvailableDateService) {
    suspend fun getAvailableDateById(availableDateId: Long, token: String): Resource<AvailableDate> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"

        val response = availableDateService.getAvailableDateById(availableDateId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { availableDateDto ->
                val availableDate = availableDateDto.toAvailableDate()
                return@withContext Resource.Success(availableDate)
            }
            return@withContext Resource.Error(message = "Error al obtener la fecha disponible")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getAvailableDatesByAdvisor(advisorId: Long, token: String): Resource<List<AvailableDate>> = withContext(
        Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = availableDateService.getAvailableDates(bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { availableDateDtos ->
                val availableDates = availableDateDtos.map { it.toAvailableDate() }
                val filteredAvailableDates = availableDates.filter { it.advisorId == advisorId }
                return@withContext Resource.Success(data = filteredAvailableDates)
            }
            return@withContext Resource.Error(message = "Error al obtener fechas disponibles")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun createAvailableDate(token: String, availableDate: CreateAvailableDate): Resource<AvailableDate> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = availableDateService.createAvailableDate(bearerToken, availableDate)
        if (response.isSuccessful) {
            response.body()?.let { availableDateDto ->
                val availableDateCreated = availableDateDto.toAvailableDate()
                return@withContext Resource.Success(availableDateCreated)
            }
            return@withContext Resource.Error(message = "No se pudo crear la fecha disponible")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun deleteAvailableDate(availableDateId: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = availableDateService.deleteAvailableDate(availableDateId, bearerToken)
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        return@withContext Resource.Error(response.message())
    }
}