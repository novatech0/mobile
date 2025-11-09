package com.example.agrotech.data.repository.advisor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.agrotech.common.Resource
import com.example.agrotech.data.remote.advisor.AdvisorService
import com.example.agrotech.data.remote.advisor.toAdvisor
import com.example.agrotech.domain.advisor.Advisor
import javax.inject.Inject

class AdvisorRepository @Inject constructor(private val advisorService: AdvisorService) {
    suspend fun searchAdvisorByUserId(userId: Long, token: String): Resource<Advisor> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = advisorService.getAdvisor(userId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { advisorDto ->
                val advisor = advisorDto.toAdvisor()
                return@withContext Resource.Success(advisor)
            }
            return@withContext Resource.Error(message = "No se encontró asesor")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun searchAdvisorByAdvisorId(advisorId: Long, token: String): Resource<Advisor> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = advisorService.getAdvisorByAdvisorId(advisorId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { advisorDto ->
                val advisor = advisorDto.toAdvisor()
                return@withContext Resource.Success(advisor)
            }
            return@withContext Resource.Error(message = "No se encontró asesor")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun isUserAdvisor(userId: Long, token: String): Boolean = withContext(Dispatchers.IO) {
        val result = searchAdvisorByUserId(userId, token)
        return@withContext result is Resource.Success
    }
}