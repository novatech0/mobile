package com.example.agrotech.data.repository.farmer

import com.example.agrotech.common.Resource
import com.example.agrotech.data.remote.farmer.FarmerService
import com.example.agrotech.data.remote.farmer.toFarmer
import com.example.agrotech.domain.farmer.Farmer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FarmerRepository @Inject constructor(private val farmerService: FarmerService) {
    suspend fun searchFarmerByUserId(userId: Long, token: String): Resource<Farmer> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = farmerService.getFarmerByUserId(userId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { farmerDto ->
                val farmer = farmerDto.toFarmer()
                return@withContext Resource.Success(farmer)
            }
            return@withContext Resource.Error(message = "No se encontró granjero")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun searchFarmerByFarmerId(farmerId: Long, token: String): Resource<Farmer> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = farmerService.getFarmer(farmerId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { farmerDto ->
                val farmer = farmerDto.toFarmer()
                return@withContext Resource.Success(farmer)
            }
            return@withContext Resource.Error(message = "No se encontró granjero")
        }
        return@withContext Resource.Error(response.message())
    }
}