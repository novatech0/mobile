package com.example.agrotech.data.repository.profile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.agrotech.common.Resource
import com.example.agrotech.data.remote.profile.ProfileService
import com.example.agrotech.data.remote.profile.toProfile
import com.example.agrotech.domain.profile.CreateProfile
import com.example.agrotech.domain.profile.Profile
import com.example.agrotech.domain.profile.UpdateProfile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileRepository(private val profileService: ProfileService) {
    suspend fun createProfile(token: String, profile: CreateProfile, isAdvisor: Boolean): Resource<Profile> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error("Un token es requerido")

        val bearerToken = "Bearer $token"

        val userId = profile.userId.toString().toRequestBody()
        val firstName = profile.firstName.toRequestBody()
        val lastName = profile.lastName.toRequestBody()
        val city = profile.city.toRequestBody()
        val country = profile.country.toRequestBody()
        val birthDate = profile.birthDate.toRequestBody()
        val description = profile.description.toRequestBody()

        val imagePart = profile.photo?.let { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("photo", file.name, requestFile)
        } ?: return@withContext Resource.Error("La foto es requerida")

        val occupation = if (isAdvisor) {
            profile.occupation.toRequestBody()
        } else {
            "".toRequestBody()
        }

        val experience = if (isAdvisor) {
            profile.experience.toString()?.toRequestBody()
        } else {
            "0".toRequestBody()
        }

        val response = profileService.createProfile(
            bearerToken,
            userId,
            firstName,
            lastName,
            city,
            country,
            birthDate,
            imagePart,
            occupation,
            description,
            experience,
        )

        if (response.isSuccessful) {
            response.body()?.let { dto ->
                val createdProfile = dto.toProfile()
                return@withContext Resource.Success(createdProfile)
            }
            return@withContext Resource.Error("No se pudo crear el perfil")
        }

        return@withContext Resource.Error(response.message())
    }

    suspend fun searchProfile(userId: Long, token: String): Resource<Profile> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = profileService.getProfile(userId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { profileDto ->
                val profile = profileDto.toProfile()
                return@withContext Resource.Success(data = profile)
            }
            return@withContext Resource.Error(message = "No se encontró perfil")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun searchFarmerProfile(farmerId: Long, token: String): Resource<Profile> = withContext(Dispatchers.IO) {
        try {
            if (token.isBlank()) {
                return@withContext Resource.Error(message = "Un token es requerido")
            }
            val bearerToken = "Bearer $token"
            val response = profileService.getProfile(farmerId, bearerToken)
            if (response.isSuccessful) {
                response.body()?.let { profileDto ->
                    val profile = profileDto.toProfile()
                    return@withContext Resource.Success(data = profile)
                }
                return@withContext Resource.Error(message = "No se encontró el perfil del granjero")
            }
            return@withContext Resource.Error(message = "Error fetching farmer profile: ${response.message()}")
        } catch (e: Exception) {
            return@withContext Resource.Error(message = "Exception: ${e.localizedMessage}")
        }
    }

    suspend fun getAdvisorList(token: String): Resource<List<Profile>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = profileService.getAdvisors(bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { profileDtos ->
                val advisors = profileDtos.map { it.toProfile() }
                return@withContext Resource.Success(data = advisors)
            }
            return@withContext Resource.Error(message = "Error al obtener lista de asesores")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun updateProfile(token: String, id: Long, profile: UpdateProfile): Resource<Profile> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error(message = "Un token es requerido")

        val bearerToken = "Bearer $token"

        // Campos requeridos
        val firstName = profile.firstName.toRequestBody()
        val lastName = profile.lastName.toRequestBody()
        val city = profile.city.toRequestBody()
        val country = profile.country.toRequestBody()
        val birthDate = profile.birthDate.toRequestBody()

        // Campos opcionales
        val description = profile.description.toRequestBody()
        val occupation = profile.occupation.toRequestBody()
        val experience = profile.experience.toString().toRequestBody()

        // Imagen opcional
        val imagePart = profile.photo?.let { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("photo", file.name, requestFile)
        }

        val response = profileService.updateProfile(
            bearerToken,
            id,
            firstName,
            lastName,
            city,
            country,
            birthDate,
            imagePart,
            occupation,
            description,
            experience
        )

        if (response.isSuccessful) {
            response.body()?.let { profileDto ->
                val updatedProfile = profileDto.toProfile()
                return@withContext Resource.Success(updatedProfile)
            }
            return@withContext Resource.Error(message = "No se pudo actualizar el perfil")
        }
        return@withContext Resource.Error(response.message())
    }




}