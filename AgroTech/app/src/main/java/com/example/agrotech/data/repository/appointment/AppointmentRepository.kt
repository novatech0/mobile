package com.example.agrotech.data.repository.appointment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.agrotech.common.Resource
import com.example.agrotech.data.remote.appointment.AppointmentService
import com.example.agrotech.domain.appointment.CreateAppointment
import com.example.agrotech.data.remote.appointment.toAppointment
import com.example.agrotech.domain.appointment.Appointment
import com.example.agrotech.domain.appointment.UpdateAppointment
import javax.inject.Inject

class AppointmentRepository @Inject constructor(private val appointmentService: AppointmentService) {
    suspend fun getAppointmentById(id: Long, token: String): Resource<Appointment> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = appointmentService.getAppointment(id, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { appointmentDto ->
                val appointment = appointmentDto.toAppointment()
                return@withContext Resource.Success(appointment)
            }
            return@withContext Resource.Error(message = "Cita no encontrada")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getAllAppointments(token: String): Resource<List<Appointment>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = appointmentService.getAllAppointments(bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { appointmentList ->
                val appointments = appointmentList.map { it.toAppointment() }
                return@withContext Resource.Success(appointments)
            }
            return@withContext Resource.Error(message = "No se encontraron citas")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getAppointmentsByAdvisor(advisorId: Long, token: String): Resource<List<Appointment>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = appointmentService.getAppointmentsByAdvisor(advisorId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { appointmentList ->
                val appointments = appointmentList.map { it.toAppointment() }
                return@withContext Resource.Success(appointments)
            }
            return@withContext Resource.Error(message = "No se encontraron citas")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getAppointmentsByFarmer(farmerId: Long, token: String): Resource<List<Appointment>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = appointmentService.getAppointmentsByFarmer(farmerId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { appointmentList ->
                val appointments = appointmentList.map { it.toAppointment() }
                return@withContext Resource.Success(appointments)
            }
            return@withContext Resource.Error(message = "No se encontraron citas")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getAppointmentsByAdvisorAndFarmer(advisorId: Long, farmerId: Long, token: String): Resource<List<Appointment>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = appointmentService.getAppointmentsByAdvisorAndFarmer(advisorId, farmerId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { appointmentList ->
                val appointments = appointmentList.map { it.toAppointment() }
                return@withContext Resource.Success(appointments)
            }
            return@withContext Resource.Error(message = "No se encontraron citas")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun createAppointment(token: String, appointment: CreateAppointment): Resource<Appointment> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = appointmentService.createAppointment(bearerToken, appointment)
        if (response.isSuccessful) {
            response.body()?.let { appointmentDto ->
                val appointmentCreated = appointmentDto.toAppointment()
                return@withContext Resource.Success(appointmentCreated)
            }
            return@withContext Resource.Error(message = "No se pudo crear la cita")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun deleteAppointment(id: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = appointmentService.deleteAppointment(id, bearerToken)
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun updateAppointment(id: Long, token: String, appointment: UpdateAppointment): Resource<Appointment> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = appointmentService.updateAppointment(id, bearerToken, appointment)
        if (response.isSuccessful) {
            response.body()?.let { appointmentDto ->
                val updatedAppointment = appointmentDto.toAppointment()
                return@withContext Resource.Success(updatedAppointment)
            }
            return@withContext Resource.Error(message = "No se pudo actualizar la cita")
        }
        return@withContext Resource.Error(response.message())
    }


}