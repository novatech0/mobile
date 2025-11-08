package com.example.agrotech.data.repository.enclosure

import com.example.agrotech.common.Resource
import com.example.agrotech.data.remote.enclosure.EnclosureService
import com.example.agrotech.data.remote.enclosure.toEnclosure
import com.example.agrotech.domain.enclosure.Enclosure
import com.example.agrotech.domain.enclosure.toEnclosureDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EnclosureRepository @Inject constructor(private val enclosureService: EnclosureService) {

    suspend fun getEnclosuresByFarmerId(token: String, farmerId: Long): Resource<List<Enclosure>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = enclosureService.getEnclosuresByFarmerId(bearerToken, farmerId)
        if (response.isSuccessful) {
            response.body()?.let { enclosuresDto ->
                val enclosures = enclosuresDto.map { it.toEnclosure() }
                return@withContext Resource.Success(enclosures)
            }
            return@withContext Resource.Error(message = "Error al obtener los cercados")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getEnclosureById(token: String, id: Long): Resource<Enclosure> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = enclosureService.getEnclosureById(bearerToken, id)
        if (response.isSuccessful) {
            response.body()?.let { enclosureDto ->
                val enclosure = enclosureDto.toEnclosure()
                return@withContext Resource.Success(enclosure)
            }
            return@withContext Resource.Error(message = "Error al obtener el cercado")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun createEnclosure(token: String, enclosure: Enclosure): Resource<Enclosure> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = enclosureService.createEnclosure(bearerToken, enclosure.toEnclosureDto())
        if (response.isSuccessful) {
            response.body()?.let { enclosureDto ->
                val createdEnclosure = enclosureDto.toEnclosure()
                return@withContext Resource.Success(createdEnclosure)
            }
            return@withContext Resource.Error(message = "No se pudo crear el cercado")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun updateEnclosure(token: String, enclosure: Enclosure): Resource<Enclosure> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = enclosureService.updateEnclosure(bearerToken, enclosure.id, enclosure.toEnclosureDto())
        if (response.isSuccessful) {
            response.body()?.let { enclosureDto ->
                val updatedEnclosure = enclosureDto.toEnclosure()
                return@withContext Resource.Success(updatedEnclosure)
            }
            return@withContext Resource.Error(message = "No se pudo actualizar el cercado")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun deleteEnclosure(token: String, enclosureId: Long): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = enclosureService.deleteEnclosure(bearerToken, enclosureId)
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        return@withContext Resource.Error(response.message())
    }
}
